package agendamento;

import java.util.Map;

/**
 * Simple task used for demonstration. It prints a message, sleeps for a while
 * and checks for cancellation between iterations.
 */
public class ExemploTask implements JobTask {

    @Override
    public void execute(Map<String, Object> params, JobContext ctx) throws Exception {
        String mensagem = String.valueOf(params.getOrDefault("mensagem", "Executando ExemploTask"));
        for (int i = 0; i < 5; i++) {
            if (ctx.isCancelled()) {
                ctx.log("Execução cancelada");
                return;
            }
            ctx.log(mensagem + " - passo " + i);
            Thread.sleep(500);
        }
    }
}
