package conexao;

import infra.EntityManagerUtil;
import infra.Logger;
import javax.persistence.EntityManager;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
        URL = (String) props.get("javax.persistence.jdbc.url");
        USER = (String) props.get("javax.persistence.jdbc.user");
        String pwd = (String) props.get("javax.persistence.jdbc.password");
        if ("****".equals(pwd)) {
            pwd = loadPasswordFromPersistenceXml();
        }
        PASSWORD = pwd;
        DRIVER = (String) props.get("javax.persistence.jdbc.driver");
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            Logger.error("Driver JDBC não encontrado: " + DRIVER, e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConnectionFactory() {
    }

    private static String loadPasswordFromPersistenceXml() {
        try (InputStream in = ConnectionFactory.class.getClassLoader()
                .getResourceAsStream("META-INF/persistence.xml")) {
            if (in == null) {
                Logger.error("Arquivo persistence.xml não encontrado", null);
                return null;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(in);
            NodeList props = doc.getElementsByTagName("property");
            for (int i = 0; i < props.getLength(); i++) {
                Element el = (Element) props.item(i);
                if ("javax.persistence.jdbc.password".equals(el.getAttribute("name"))) {
                    return el.getAttribute("value");
                }
            }
        } catch (Exception e) {
            Logger.error("Erro ao carregar a senha do persistence.xml", e);
        }
        return null;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
