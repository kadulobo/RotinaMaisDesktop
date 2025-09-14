package conexao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Carrega as configurações de conexão com o banco de dados.
 */
public final class DatabaseConfig {

    private static final String FILE_NAME = "database.properties";
    private static final DatabaseConfig INSTANCE = new DatabaseConfig();
    private final Properties props = new Properties();

    private DatabaseConfig() {
        Path path = Paths.get(FILE_NAME);
        if (Files.notExists(path)) {
            throw new IllegalStateException("Arquivo de configuração " + FILE_NAME + " não encontrado");
        }
        try (InputStream in = Files.newInputStream(path)) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar o arquivo de configuração do banco", e);
        }
    }

    public static DatabaseConfig get() {
        return INSTANCE;
    }

    public String getUser() {
        return props.getProperty("db.user");
    }

    public String getPassword() {
        return props.getProperty("db.password");
    }

    public String getDatabase() {
        return props.getProperty("db.name");
    }

    public int getPort() {
        return Integer.parseInt(props.getProperty("db.port", "5432"));
    }

    public String getSchema() {
        return props.getProperty("db.schema");
    }

    public boolean isEmbedded() {
        return Boolean.parseBoolean(props.getProperty("db.embedded", "false"));
    }

    /**
     * Constrói a URL JDBC baseado nas configurações atuais.
     */
    public String getJdbcUrl() {
        return "jdbc:postgresql://localhost:" + getPort() + "/" + getDatabase()
                + "?currentSchema=" + getSchema();
    }
}
