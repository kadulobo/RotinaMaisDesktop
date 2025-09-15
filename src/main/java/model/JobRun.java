package model;

import java.time.Instant;

/**
 * Represents an execution of a job.
 */
public class JobRun {
    private long id;
    private long jobId;
    private Instant filaEm;
    private Instant iniciouEm;
    private Instant terminouEm;
    private RunStatus status = RunStatus.QUEUED;
    private String erroMsg;
    private Long pidSubprocess;
    private long durationMs;

    // getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getJobId() { return jobId; }
    public void setJobId(long jobId) { this.jobId = jobId; }
    public Instant getFilaEm() { return filaEm; }
    public void setFilaEm(Instant filaEm) { this.filaEm = filaEm; }
    public Instant getIniciouEm() { return iniciouEm; }
    public void setIniciouEm(Instant iniciouEm) { this.iniciouEm = iniciouEm; }
    public Instant getTerminouEm() { return terminouEm; }
    public void setTerminouEm(Instant terminouEm) { this.terminouEm = terminouEm; }
    public RunStatus getStatus() { return status; }
    public void setStatus(RunStatus status) { this.status = status; }
    public String getErroMsg() { return erroMsg; }
    public void setErroMsg(String erroMsg) { this.erroMsg = erroMsg; }
    public Long getPidSubprocess() { return pidSubprocess; }
    public void setPidSubprocess(Long pidSubprocess) { this.pidSubprocess = pidSubprocess; }
    public long getDurationMs() { return durationMs; }
    public void setDurationMs(long durationMs) { this.durationMs = durationMs; }
}
