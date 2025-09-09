package conexao;

import infra.EntityManagerUtil;
import infra.Logger;
import jakarta.persistence.EntityManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Factory responsible for creating JDBC connections based on the
 * configuration used by JPA's {@link EntityManagerUtil}. Each invocation of
 * {@link #getConnection()} returns a new {@link Connection} that must be
 * closed by the caller.
 */
public final class ConnectionFactory {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;
    private static final String DRIVER;

    static {
        EntityManager em = EntityManagerUtil.getEntityManager();
        Map<String, Object> props;
        try {
            props = em.getEntityManagerFactory().getProperties();
        } finally {
            em.close();
        }
        URL = (String) props.get("jakarta.persistence.jdbc.url");
        USER = (String) props.get("jakarta.persistence.jdbc.user");
        PASSWORD = (String) props.get("jakarta.persistence.jdbc.password");
        DRIVER = (String) props.get("jakarta.persistence.jdbc.driver");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Logger.error("Driver JDBC não encontrado: " + DRIVER, e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConnectionFactory() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
