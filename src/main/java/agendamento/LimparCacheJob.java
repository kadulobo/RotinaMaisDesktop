package agendamento;

import java.io.File;
import java.util.Map;

/**
 * Deletes temporary files from a directory provided by parameter 'dirTemp'.
 */
public class LimparCacheJob implements JobTask {

    @Override
    public void execute(Map<String, Object> params, JobContext ctx) throws Exception {
        String dir = String.valueOf(params.getOrDefault("dirTemp", System.getProperty("java.io.tmpdir")));
        File tmp = new File(dir);
        ctx.log("Limpando diretório " + tmp.getAbsolutePath());
        File[] files = tmp.listFiles();
        if (files != null) {
            for (File f : files) {
                if (ctx.isCancelled()) {
                    ctx.log("Cancelado durante limpeza");
                    return;
                }
                if (f.delete()) {
                    ctx.log("Removido: " + f.getName());
                }
            }
        }
    }
}
