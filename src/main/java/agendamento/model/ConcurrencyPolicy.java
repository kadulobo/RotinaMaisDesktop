package agendamento.model;

/**
 * How the scheduler should behave when a job is triggered while another run is
 * already in progress.
 */
public enum ConcurrencyPolicy {
    /** Allow concurrent executions */
    ALLOW,
    /** Forbid new run while one is executing */
    FORBID,
    /** Cancel current run and replace with new */
    REPLACE;

    public static ConcurrencyPolicy fromString(String s) {
        for (ConcurrencyPolicy p : values()) {
            if (p.name().equalsIgnoreCase(s)) {
                return p;
            }
        }
        return ALLOW;
    }
}
