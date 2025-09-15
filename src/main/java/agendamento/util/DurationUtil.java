package agendamento.util;

import java.time.Duration;

/** Utility class to format durations in a human readable form. */
public final class DurationUtil {
    private DurationUtil() {}

    /**
     * Formats the given duration in milliseconds using a short human readable
     * representation such as "2m 5s" or "734ms".
     */
    public static String format(long ms) {
        if (ms < 1000) {
            return ms + "ms";
        }
        Duration d = Duration.ofMillis(ms);
        long minutes = d.toMinutes();
        long seconds = d.minusMinutes(minutes).getSeconds();
        StringBuilder sb = new StringBuilder();
        if (minutes > 0) {
            sb.append(minutes).append("m");
            if (seconds > 0) {
                sb.append(' ').append(seconds).append("s");
            }
        } else {
            sb.append(seconds).append("s");
        }
        return sb.toString();
    }
}
