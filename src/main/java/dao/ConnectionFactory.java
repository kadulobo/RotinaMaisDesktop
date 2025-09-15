package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import conexao.DatabaseConfig;

/**
 * Simple JDBC connection factory that reads configuration from
 * {@link conexao.DatabaseConfig}.
 */
public final class ConnectionFactory {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String DRIVER;

    static {
        DatabaseConfig config = DatabaseConfig.get();
        URL = config.getJdbcUrl();
        USER = config.getUser();
        PASSWORD = config.getPassword();
        DRIVER = "org.postgresql.Driver";
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConnectionFactory() {}

    /**
     * Obtains a new JDBC connection. The caller is responsible for closing it.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
