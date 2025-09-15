package model;

/**
 * Possible statuses for a job or step run.
 */
public enum RunStatus {
    QUEUED,
    RUNNING,
    SUCCESS,
    FAILED,
    ABORTED,
    SKIPPED
}
