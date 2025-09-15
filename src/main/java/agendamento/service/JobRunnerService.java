package agendamento.service;

import agendamento.JobContext;
import agendamento.JobTask;
import dao.JobDao;
import model.*;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.sql.SQLException;

/**
 * Executes jobs defined in the database. The service uses a
 * {@link ScheduledExecutorService} to perform asynchronous executions.
 */
public class JobRunnerService {

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(
            Runtime.getRuntime().availableProcessors(), r -> {
                Thread t = new Thread(r);
                t.setName(String.format("agendamento-%d", t.getId()));
                t.setDaemon(true);
                return t;
            });
    private final JobDao dao;
    private final Map<Long, Future<?>> runs = new ConcurrentHashMap<>();

    public JobRunnerService(JobDao dao) {
        this.dao = dao;
    }

    /**
     * Triggers execution of a job immediately.
     *
     * @return id of the created job_run record
     */
    public long executarAgora(long jobId) throws Exception {
        Job job = dao.listJobsAtivos().stream().filter(j -> j.getId() == jobId).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Job não encontrado: " + jobId));
        long runId = dao.insertRunQueued(jobId);
        Future<?> f = executor.submit(() -> runJob(job, runId));
        runs.put(runId, f);
        return runId;
    }

    /** Cancels a running job. */
    public void cancelar(long runId) {
        Future<?> f = runs.get(runId);
        if (f != null) {
            f.cancel(true);
        }
    }

    private void runJob(Job job, long runId) {
        JobRun run = new JobRun();
        run.setId(runId);
        run.setJobId(job.getId());
        run.setIniciouEm(Instant.now());
        run.setStatus(RunStatus.RUNNING);
        try {
            dao.updateRunStatus(run);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<JobStep> steps = dao.listSteps(job.getId());
            Map<Long, StepRun> stepRuns = new HashMap<>();
            for (JobStep step : steps) {
                long srId = dao.ensureStepRunQueued(runId, step);
                StepRun sr = new StepRun();
                sr.setId(srId);
                sr.setRunId(runId);
                sr.setStepId(step.getId());
                sr.setOrdemCache(step.getOrdem());
                sr.setIniciouEm(Instant.now());
                sr.setStatus(RunStatus.RUNNING);
                dao.updateStepRunStatus(sr);
                RunStatus st = executeStep(job, step, sr, runId);
                sr.setStatus(st);
                sr.setTerminouEm(Instant.now());
                sr.setDurationMs(Duration.between(sr.getIniciouEm(), sr.getTerminouEm()).toMillis());
                dao.updateStepRunStatus(sr);
                stepRuns.put(step.getId(), sr);
                if (st != RunStatus.SUCCESS && !step.isContinueOnFail()) {
                    throw new RuntimeException("Step falhou: " + step.getNome());
                }
            }
            run.setStatus(RunStatus.SUCCESS);
        } catch (Exception e) {
            run.setStatus(RunStatus.FAILED);
            run.setErroMsg(e.getMessage());
        } finally {
            run.setTerminouEm(Instant.now());
            run.setDurationMs(Duration.between(run.getIniciouEm(), run.getTerminouEm()).toMillis());
            try {
                dao.updateRunStatus(run);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private RunStatus executeStep(Job job, JobStep step, StepRun sr, long runId) {
        try (DefaultJobContext ctx = new DefaultJobContext(runId, dao, sr)) {
            Map<String, Object> params = parseParams(step.getParametros());
            String className = step.getHandlerClass() != null ? step.getHandlerClass() : job.getHandlerClass();
            String methodName = step.getHandlerMethod() != null ? step.getHandlerMethod() : job.getHandlerMethod();
            boolean handlerStatic = step.isHandlerStatic() || job.isHandlerStatic();
            boolean runSub = step.isRunInSubprocess() || job.isRunInSubprocess();
            if (runSub) {
                return runSubprocess(className, params, ctx, sr);
            } else {
                runReflection(className, methodName, handlerStatic, params, ctx);
                return RunStatus.SUCCESS;
            }
        } catch (Exception e) {
            sr.setErroMsg(e.getMessage());
            return RunStatus.FAILED;
        }
    }

    private void runReflection(String className, String methodName, boolean isStatic,
                               Map<String, Object> params, JobContext ctx) throws Exception {
        Class<?> clazz = Class.forName(className);
        if (JobTask.class.isAssignableFrom(clazz)) {
            JobTask task = (JobTask) clazz.getDeclaredConstructor().newInstance();
            task.execute(params, ctx);
            return;
        }
        if (methodName == null || methodName.isEmpty()) {
            methodName = "execute";
        }
        Method m = clazz.getMethod(methodName, Map.class, JobContext.class);
        Object target = isStatic ? null : clazz.getDeclaredConstructor().newInstance();
        m.invoke(target, params, ctx);
    }

    private RunStatus runSubprocess(String className, Map<String, Object> params, JobContext ctx, StepRun sr)
            throws Exception {
        List<String> cmd = new ArrayList<>();
        cmd.add("java");
        cmd.add("-cp");
        cmd.add(System.getProperty("java.class.path"));
        cmd.add(className);
        ProcessBuilder pb = new ProcessBuilder(cmd);
        File logFile = File.createTempFile("step-", ".log");
        sr.setLogPath(logFile.getAbsolutePath());
        pb.redirectErrorStream(true);
        pb.redirectOutput(logFile);
        Process p = pb.start();
        int exit = p.waitFor();
        return exit == 0 ? RunStatus.SUCCESS : RunStatus.FAILED;
    }

    private Map<String, Object> parseParams(String parametros) {
        Map<String, Object> map = new HashMap<>();
        if (parametros == null || parametros.trim().isEmpty()) {
            return map;
        }
        // very small parser: key1=value1;key2=value2
        for (String part : parametros.split(";")) {
            int idx = part.indexOf('=');
            if (idx > 0) {
                String k = part.substring(0, idx).trim();
                String v = part.substring(idx + 1).trim();
                map.put(k, v);
            }
        }
        return map;
    }

    /**
     * Default implementation of {@link JobContext} used by the runner.
     */
    private static class DefaultJobContext implements JobContext {
        private final long runId;
        private final JobDao dao;
        private final StepRun stepRun;
        private volatile boolean cancelled;
        private PrintWriter fileWriter;

        DefaultJobContext(long runId, JobDao dao, StepRun stepRun) {
            this.runId = runId;
            this.dao = dao;
            this.stepRun = stepRun;
        }

        @Override
        public void log(String message) {
            try {
                dao.appendRunLog(runId, message);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (fileWriter != null) {
                fileWriter.println(message);
                fileWriter.flush();
            }
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void cancel() {
            cancelled = true;
        }

        @Override
        public void setLogFile(PrintWriter writer) {
            this.fileWriter = writer;
        }

        @Override
        public void close() {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }
}
