package agendamento.util;

import java.time.Duration;

/** Utility class to format durations in a human readable form. */
public final class DurationUtil {
    private DurationUtil() {}

    /**
     * Formats the given duration in milliseconds as "735ms/2m 12s" style.
     */
    public static String format(long ms) {
        Duration d = Duration.ofMillis(ms);
        long minutes = d.toMinutes();
        long seconds = d.minusMinutes(minutes).getSeconds();
        long millis = d.minusMinutes(minutes).minusSeconds(seconds).toMillis();
        StringBuilder sb = new StringBuilder();
        if (minutes > 0) {
            sb.append(minutes).append("m ");
        }
        if (seconds > 0) {
            sb.append(seconds).append("s");
        }
        return ms + "ms/" + sb.toString().trim();
    }
}
