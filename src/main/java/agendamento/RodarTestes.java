package agendamento;

import java.util.Map;
import java.util.Random;

/**
 * Step that simulates running tests. It randomly fails to demonstrate UI
 * behaviour on failures.
 */
public class RodarTestes implements JobTask {
    private final Random rnd = new Random();

    @Override
    public void execute(Map<String, Object> params, JobContext ctx) throws Exception {
        ctx.log("Rodando testes...");
        Thread.sleep(500);
        if (rnd.nextInt(10) == 0) {
            throw new RuntimeException("Falha aleatória nos testes");
        }
    }
}
