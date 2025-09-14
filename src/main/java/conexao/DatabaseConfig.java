package conexao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Carrega as configurações de conexão com o banco de dados.
 * Caso o arquivo {@code database.properties} não exista na
 * pasta de trabalho, ele é criado com valores padrão.
 */
public final class DatabaseConfig {

    private static final String FILE_NAME = "database.properties";
    private static final DatabaseConfig INSTANCE = new DatabaseConfig();
    private final Properties props = new Properties();

    private DatabaseConfig() {
        Path path = Paths.get(FILE_NAME);
        try {
            if (Files.notExists(path)) {
                // Copia do classpath ou cria um arquivo novo com valores padrão
                try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {
                    if (in != null) {
                        Files.copy(in, path);
                    } else {
                        props.setProperty("db.port", "5432");
                        props.setProperty("db.name", "rotinamais");
                        props.setProperty("db.schema", "rotinamais");
                        props.setProperty("db.user", "kadu");
                        props.setProperty("db.password", "123");
                        props.setProperty("db.embedded", "true");
                        try (OutputStream out = Files.newOutputStream(path)) {
                            props.store(out, "Database configuration");
                        }
                    }
                }
            }
            try (InputStream in = Files.newInputStream(path)) {
                props.load(in);
            }
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
        return Boolean.parseBoolean(props.getProperty("db.embedded", "true"));
    }

    /**
     * Constrói a URL JDBC baseado nas configurações atuais.
     */
    public String getJdbcUrl() {
        return "jdbc:postgresql://localhost:" + getPort() + "/" + getDatabase()
                + "?currentSchema=" + getSchema();
    }
}
