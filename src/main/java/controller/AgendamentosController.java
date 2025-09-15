package controller;

import dao.JobDao;
import model.*;
import Agendamento.service.JobRunnerService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Controller layer used by the UI to interact with the scheduling service and
 * DAO.
 */
public class AgendamentosController {
    private final JobDao dao;
    private final JobRunnerService service;

    public AgendamentosController(JobDao dao, JobRunnerService service) {
        this.dao = dao;
        this.service = service;
    }

    public List<Job> listJobs() throws SQLException {
        return dao.listJobsAtivos();
    }

    public List<JobStep> listSteps(long jobId) throws SQLException {
        return dao.listSteps(jobId);
    }

    public long executarAgora(long jobId) throws Exception {
        return service.executarAgora(jobId);
    }

    public void cancelarRun(long runId) {
        service.cancelar(runId);
    }

    public List<JobRun> listRuns(long jobId, int limit, int offset) throws SQLException {
        return dao.listRuns(jobId, limit, offset);
    }

    public Map<Long, StepRun> mapStepRuns(long runId) throws SQLException {
        return dao.mapStepRuns(runId);
    }

    /** Opens a log file if present, otherwise reads from run_log table. */
    public String abrirLog(StepRun sr) throws Exception {
        if (sr.getLogPath() != null) {
            try {
                return new String(Files.readAllBytes(Paths.get(sr.getLogPath())));
            } catch (IOException e) {
                return "Erro ao ler log: " + e.getMessage();
            }
        }
        // fallback: no file, so read from run_log (simplified)
        StringBuilder sb = new StringBuilder();
        // In a full implementation we'd query run_log; simplified here.
        sb.append("(log não disponível)");
        return sb.toString();
    }
}
