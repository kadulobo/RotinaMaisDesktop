package agendamento.dao;

import agendamento.model.*;

import java.sql.*;
import java.time.Instant;
import java.util.*;

/**
 * Data access object handling all queries for the scheduling module. SQL
 * statements are centralized in this class and use {@link PreparedStatement} to
 * avoid SQL injection.
 */
public class JobDao {

    private static final String SQL_LIST_JOBS =
            "select id_job, nome, handler_class, handler_method, handler_static, run_in_subprocess, working_dir, jvm_args, env_vars, politica_concorr, max_retries, retry_backoff_s, timeout_s " +
            "from job where ativo = true order by nome";

    private static final String SQL_LIST_STEPS =
            "select * from job_step where id_job = ? and habilitado = true order by ordem";

    private static final String SQL_INSERT_RUN =
            "insert into job_run(id_job, fila_em, status) values(?, now(), ?) returning id_run";

    private static final String SQL_UPDATE_RUN_STATUS =
            "update job_run set iniciou_em=?, terminou_em=?, status=?, erro_msg=?, pid_subprocess=?, duration_ms=? where id_run=?";

    private static final String SQL_INSERT_STEP_RUN =
            "insert into step_run(id_run, id_step, ordem_cache, iniciou_em, status) values(?,?,?,?,?) returning id_step_run";

    private static final String SQL_UPDATE_STEP_RUN =
            "update step_run set iniciou_em=?, terminou_em=?, status=?, erro_msg=?, log_path=?, duration_ms=? where id_step_run=?";

    /** Lists active jobs. */
    public List<Job> listJobsAtivos() throws SQLException {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_LIST_JOBS);
             ResultSet rs = ps.executeQuery()) {
            List<Job> out = new ArrayList<>();
            while (rs.next()) {
                Job j = new Job();
                j.setId(rs.getLong("id_job"));
                j.setNome(rs.getString("nome"));
                j.setHandlerClass(rs.getString("handler_class"));
                j.setHandlerMethod(rs.getString("handler_method"));
                j.setHandlerStatic(rs.getBoolean("handler_static"));
                j.setRunInSubprocess(rs.getBoolean("run_in_subprocess"));
                j.setWorkingDir(rs.getString("working_dir"));
                j.setJvmArgs(rs.getString("jvm_args"));
                j.setEnvVars(rs.getString("env_vars"));
                j.setPolicy(ConcurrencyPolicy.fromString(rs.getString("politica_concorr")));
                j.setMaxRetries(rs.getInt("max_retries"));
                j.setRetryBackoff(rs.getInt("retry_backoff_s"));
                j.setTimeout(rs.getInt("timeout_s"));
                out.add(j);
            }
            return out;
        }
    }

    /** Returns steps of the given job. */
    public List<JobStep> listSteps(long jobId) throws SQLException {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_LIST_STEPS)) {
            ps.setLong(1, jobId);
            try (ResultSet rs = ps.executeQuery()) {
                List<JobStep> out = new ArrayList<>();
                while (rs.next()) {
                    JobStep s = new JobStep();
                    s.setId(rs.getLong("id_step"));
                    s.setJobId(jobId);
                    s.setOrdem(rs.getInt("ordem"));
                    s.setNome(rs.getString("nome"));
                    s.setHabilitado(rs.getBoolean("habilitado"));
                    s.setHandlerClass(rs.getString("handler_class"));
                    s.setHandlerMethod(rs.getString("handler_method"));
                    s.setHandlerStatic(rs.getBoolean("handler_static"));
                    s.setRunInSubprocess(rs.getBoolean("run_in_subprocess"));
                    s.setWorkingDir(rs.getString("working_dir"));
                    s.setJvmArgs(rs.getString("jvm_args"));
                    s.setEnvVars(rs.getString("env_vars"));
                    s.setParametros(rs.getString("parametros"));
                    s.setCondicaoExpr(rs.getString("condicao_expr"));
                    s.setContinueOnFail(rs.getBoolean("continue_on_fail"));
                    s.setTimeout(rs.getInt("timeout_s"));
                    s.setMaxRetries(rs.getInt("max_retries"));
                    s.setRetryBackoff(rs.getInt("retry_backoff_s"));
                    out.add(s);
                }
                return out;
            }
        }
    }

    /**
     * Lists recent runs for a job using simple pagination.
     */
    public List<JobRun> listRuns(long jobId, int limit, int offset) throws SQLException {
        String sql = "select * from job_run where id_job=? order by id_run desc limit ? offset ?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, jobId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                List<JobRun> out = new ArrayList<>();
                while (rs.next()) {
                    JobRun r = new JobRun();
                    r.setId(rs.getLong("id_run"));
                    r.setJobId(jobId);
                    r.setFilaEm(rs.getTimestamp("fila_em").toInstant());
                    Timestamp ini = rs.getTimestamp("iniciou_em");
                    if (ini != null) r.setIniciouEm(ini.toInstant());
                    Timestamp fim = rs.getTimestamp("terminou_em");
                    if (fim != null) r.setTerminouEm(fim.toInstant());
                    r.setStatus(RunStatus.valueOf(rs.getString("status")));
                    r.setErroMsg(rs.getString("erro_msg"));
                    r.setPidSubprocess((Long) rs.getObject("pid_subprocess"));
                    r.setDurationMs(rs.getLong("duration_ms"));
                    out.add(r);
                }
                return out;
            }
        }
    }

    /** Maps step runs for a given job run by step id. */
    public Map<Long, StepRun> mapStepRuns(long runId) throws SQLException {
        String sql = "select * from step_run where id_run=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, runId);
            try (ResultSet rs = ps.executeQuery()) {
                Map<Long, StepRun> map = new HashMap<>();
                while (rs.next()) {
                    StepRun sr = new StepRun();
                    sr.setId(rs.getLong("id_step_run"));
                    sr.setRunId(runId);
                    long stepId = rs.getLong("id_step");
                    sr.setStepId(stepId);
                    sr.setOrdemCache(rs.getInt("ordem_cache"));
                    Timestamp ini = rs.getTimestamp("iniciou_em");
                    if (ini != null) sr.setIniciouEm(ini.toInstant());
                    Timestamp fim = rs.getTimestamp("terminou_em");
                    if (fim != null) sr.setTerminouEm(fim.toInstant());
                    sr.setStatus(RunStatus.valueOf(rs.getString("status")));
                    sr.setErroMsg(rs.getString("erro_msg"));
                    sr.setLogPath(rs.getString("log_path"));
                    sr.setDurationMs(rs.getLong("duration_ms"));
                    map.put(stepId, sr);
                }
                return map;
            }
        }
    }

    /** Inserts a new run in QUEUED status and returns its id. */
    public long insertRunQueued(long jobId) throws SQLException {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_INSERT_RUN)) {
            ps.setLong(1, jobId);
            ps.setString(2, RunStatus.QUEUED.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    /** Updates job run status and timestamps. */
    public void updateRunStatus(JobRun run) throws SQLException {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_UPDATE_RUN_STATUS)) {
            ps.setTimestamp(1, run.getIniciouEm() != null ? Timestamp.from(run.getIniciouEm()) : null);
            ps.setTimestamp(2, run.getTerminouEm() != null ? Timestamp.from(run.getTerminouEm()) : null);
            ps.setString(3, run.getStatus().name());
            ps.setString(4, run.getErroMsg());
            if (run.getPidSubprocess() != null) {
                ps.setLong(5, run.getPidSubprocess());
            } else {
                ps.setNull(5, Types.BIGINT);
            }
            ps.setLong(6, run.getDurationMs());
            ps.setLong(7, run.getId());
            ps.executeUpdate();
        }
    }

    /** Ensures a step run record exists, creating one if necessary. */
    public long ensureStepRunQueued(long runId, JobStep step) throws SQLException {
        String sql = "select id_step_run from step_run where id_run=? and id_step=?";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, runId);
            ps.setLong(2, step.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_INSERT_STEP_RUN)) {
            ps.setLong(1, runId);
            ps.setLong(2, step.getId());
            ps.setInt(3, step.getOrdem());
            ps.setTimestamp(4, Timestamp.from(Instant.now()));
            ps.setString(5, RunStatus.QUEUED.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        }
    }

    /** Updates status info for a step run. */
    public void updateStepRunStatus(StepRun sr) throws SQLException {
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(SQL_UPDATE_STEP_RUN)) {
            ps.setTimestamp(1, sr.getIniciouEm() != null ? Timestamp.from(sr.getIniciouEm()) : null);
            ps.setTimestamp(2, sr.getTerminouEm() != null ? Timestamp.from(sr.getTerminouEm()) : null);
            ps.setString(3, sr.getStatus().name());
            ps.setString(4, sr.getErroMsg());
            ps.setString(5, sr.getLogPath());
            ps.setLong(6, sr.getDurationMs());
            ps.setLong(7, sr.getId());
            ps.executeUpdate();
        }
    }

    /** Appends a line to a run log table. Simplified implementation storing only text. */
    public void appendRunLog(long runId, String line) throws SQLException {
        String sql = "insert into run_log(id_run, momento, linha) values(?, now(), ?)";
        try (Connection c = ConnectionFactory.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, runId);
            ps.setString(2, line);
            ps.executeUpdate();
        }
    }
}
