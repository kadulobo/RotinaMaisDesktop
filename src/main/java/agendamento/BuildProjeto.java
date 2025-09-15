package agendamento;

import java.util.Map;

/**
 * Step example that just logs a compilation message.
 */
public class BuildProjeto implements JobTask {
    @Override
    public void execute(Map<String, Object> params, JobContext ctx) throws Exception {
        ctx.log("Compilando projeto...");
        Thread.sleep(500);
    }
}
