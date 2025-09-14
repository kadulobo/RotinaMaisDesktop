package Agendamento;

import java.util.Map;

/**
 * Final step that logs publication of artifacts.
 */
public class PublicarArtefatos implements JobTask {
    @Override
    public void execute(Map<String, Object> params, JobContext ctx) throws Exception {
        ctx.log("Publicando artefatos...");
        Thread.sleep(500);
    }
}
