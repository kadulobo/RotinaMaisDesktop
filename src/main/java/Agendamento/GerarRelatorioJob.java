package Agendamento;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;

/**
 * Simulates the generation of a CSV report, writing a simple file to the
 * temporary directory and logging the path.
 */
public class GerarRelatorioJob implements JobTask {

    @Override
    public void execute(Map<String, Object> params, JobContext ctx) throws Exception {
        String baseDir = System.getProperty("java.io.tmpdir");
        File out = new File(baseDir, "relatorio-" + System.currentTimeMillis() + ".csv");
        try (FileWriter w = new FileWriter(out)) {
            w.write("coluna1;coluna2\nvalor1;valor2\n");
        }
        ctx.log("Relatório gerado em: " + out.getAbsolutePath());
    }
}
