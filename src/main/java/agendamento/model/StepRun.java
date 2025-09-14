package agendamento.model;

import java.time.Instant;

/**
 * Execution status for a single step inside a job run.
 */
public class StepRun {
    private long id;
    private long runId;
    private long stepId;
    private int ordemCache;
    private Instant iniciouEm;
    private Instant terminouEm;
    private RunStatus status = RunStatus.QUEUED;
    private String erroMsg;
    private String logPath;
    private long durationMs;

    // getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getRunId() { return runId; }
    public void setRunId(long runId) { this.runId = runId; }
    public long getStepId() { return stepId; }
    public void setStepId(long stepId) { this.stepId = stepId; }
    public int getOrdemCache() { return ordemCache; }
    public void setOrdemCache(int ordemCache) { this.ordemCache = ordemCache; }
    public Instant getIniciouEm() { return iniciouEm; }
    public void setIniciouEm(Instant iniciouEm) { this.iniciouEm = iniciouEm; }
    public Instant getTerminouEm() { return terminouEm; }
    public void setTerminouEm(Instant terminouEm) { this.terminouEm = terminouEm; }
    public RunStatus getStatus() { return status; }
    public void setStatus(RunStatus status) { this.status = status; }
    public String getErroMsg() { return erroMsg; }
    public void setErroMsg(String erroMsg) { this.erroMsg = erroMsg; }
    public String getLogPath() { return logPath; }
    public void setLogPath(String logPath) { this.logPath = logPath; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
}
