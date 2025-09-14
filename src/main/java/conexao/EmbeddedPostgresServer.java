package conexao;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Inicializa e gerencia um servidor PostgreSQL embutido usando
 * a biblioteca Zoned Embedded Postgres.
 */
public final class EmbeddedPostgresServer {

    private static EmbeddedPostgres POSTGRES;

    private EmbeddedPostgresServer() {
    }

    /**
     * Inicia o servidor PostgreSQL se ainda não estiver em execução.
     */
    public static synchronized void start(DatabaseConfig cfg) {
        if (POSTGRES != null) {
            return;
        }
        try {
            // Usa um caminho absoluto para garantir que a limpeza ocorra no
            // mesmo diretório que será utilizado pelo processo do PostgreSQL
            // embutido. A biblioteca `EmbeddedPostgres` interpreta caminhos
            // relativos em relação ao diretório interno onde extrai os
            // binários, o que pode diferir do diretório de trabalho da
            // aplicação. Isso fazia com que a limpeza ocorresse em um local
            // diferente daquele passado ao `initdb`, ocasionando o erro
            // "directory exists but is not empty". Ao resolver o caminho para
            // absoluto, garantimos que tanto a limpeza quanto a inicialização
            // utilizem o mesmo diretório.
            Path dataDir = Paths.get("embedded-pg-data").toAbsolutePath();

            // A biblioteca embutida falha ao executar o initdb quando o
            // diretório de dados já existe e contém arquivos de uma
            // inicialização anterior. Para evitar o erro "directory exists but
            // is not empty" limpamos o diretório manualmente antes de iniciar
            // um novo banco.
            if (Files.exists(dataDir)) {
                try (java.util.stream.Stream<Path> walk = Files.walk(dataDir)) {
                    walk.sorted(Comparator.reverseOrder()).forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException ex) {
                            throw new IllegalStateException("Falha ao limpar diretório de dados", ex);
                        }
                    });
                }
            }

            Files.createDirectories(dataDir);
            POSTGRES = EmbeddedPostgres.builder()
                    .setPort(cfg.getPort())
                    .setDataDirectory(dataDir)
                    // desabilita limpeza para persistir dados entre reinicializações
                    .setCleanDataDirectory(false)
                    .start();
            createUserAndDatabase(cfg);
        } catch (IOException | SQLException e) {
            throw new IllegalStateException("Falha ao iniciar PostgreSQL embutido", e);
        }
    }

    private static void createUserAndDatabase(DatabaseConfig cfg) throws SQLException {
        try (Connection conn = POSTGRES.getPostgresDatabase().getConnection("postgres", "postgres");
             Statement st = conn.createStatement()) {
            try {
                st.execute("CREATE USER " + cfg.getUser() + " WITH PASSWORD '" + cfg.getPassword() + "'");
            } catch (SQLException ignore) {
                // usuário já existe
            }
            try {
                st.execute("CREATE DATABASE " + cfg.getDatabase() + " OWNER " + cfg.getUser());
            } catch (SQLException ignore) {
                // banco já existe
            }
        }
    }

    /**
     * Para o servidor se estiver ativo.
     */
    public static synchronized void stop() {
        if (POSTGRES != null) {
            try {
                POSTGRES.close();
            } catch (IOException e) {
                // ignore
            }
            POSTGRES = null;
        }
    }
}
