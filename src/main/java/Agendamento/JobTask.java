package Agendamento;

import java.util.Map;

/**
 * Contract for executable tasks in the scheduling module. Implementations
 * receive a map of parameters and a {@link JobContext} to interact with the
 * engine.
 */
public interface JobTask {
    /**
     * Executes the task.
     *
     * @param params parameters provided in the job configuration
     * @param ctx    context with logging and cancellation utilities
     * @throws Exception in case of any error during execution
     */
    void execute(Map<String, Object> params, JobContext ctx) throws Exception;
}
