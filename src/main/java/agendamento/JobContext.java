package agendamento;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Context passed to every job execution. Allows logging, accessing log files
 * and checking for cancellation requests.
 */
public interface JobContext extends Closeable {

    /**
     * Appends a line to the execution log.
     *
     * @param message message to log
     */
    void log(String message);

    /**
     * @return true if the execution was requested to cancel
     */
    boolean isCancelled();

    /**
     * Requests cooperative cancellation. Implementations should set an
     * internal flag that can be checked via {@link #isCancelled()}.
     */
    void cancel();

    /**
     * Define a file to which all subsequent logs will also be written.
     *
     * @param writer print writer for the file
     */
    void setLogFile(PrintWriter writer);

    @Override
    void close() throws IOException;
}
