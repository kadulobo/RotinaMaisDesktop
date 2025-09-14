package conexao;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        // Se o uso do PostgreSQL embutido estiver desabilitado nas configurações,
        // apenas retorna sem iniciar nada. Isso evita tentativas de inicialização
        // indevidas caso o método seja chamado inadvertidamente.
        if (!cfg.isEmbedded() || POSTGRES != null) {
            return;
        }
        try {
            // Usa um caminho absoluto para garantir que o diretório de dados
            // seja o mesmo em todas as execuções do PostgreSQL embutido. A
            // biblioteca `EmbeddedPostgres` interpreta caminhos relativos em
            // relação ao diretório interno onde extrai os binários, o que pode
            // diferir do diretório de trabalho da aplicação.
            Path dataDir = Paths.get("embedded-pg-data").toAbsolutePath();

            // Cria o diretório se ainda não existir. Não removemos arquivos
            // existentes para permitir que os dados sejam persistidos entre as
            // reinicializações da aplicação.
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
