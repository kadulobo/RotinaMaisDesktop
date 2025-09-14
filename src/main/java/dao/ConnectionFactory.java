package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Simple JDBC connection factory that reads configuration from
 * <code>application.properties</code> located in the classpath.
 */
public final class ConnectionFactory {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String DRIVER;

    static {
        Properties props = new Properties();
        try (InputStream in = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        URL = props.getProperty("db.url", "jdbc:postgresql://localhost/test");
        USER = props.getProperty("db.user", "postgres");
        PASSWORD = props.getProperty("db.password", "postgres");
        DRIVER = props.getProperty("db.driver", "org.postgresql.Driver");
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
