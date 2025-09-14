// path: src/main/java/infra/EntityManagerUtil.java
package infra;

import conexao.DatabaseConfig;
import conexao.EmbeddedPostgresServer;
import conexao.FlywayMigration;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerUtil {
    private static final EntityManagerFactory emf;

    static {
        // Carrega configurações e inicia o PostgreSQL embutido, se habilitado
        DatabaseConfig cfg = DatabaseConfig.get();
        if (cfg.isEmbedded()) {
            EmbeddedPostgresServer.start(cfg);
        }
        // Executa migrações do banco
        FlywayMigration.migrate(cfg);

        // Sobrepõe as propriedades de conexão do persistence.xml
        Map<String, String> props = new HashMap<>();
        props.put("javax.persistence.jdbc.url", cfg.getJdbcUrl());
        props.put("javax.persistence.jdbc.user", cfg.getUser());
        props.put("javax.persistence.jdbc.password", cfg.getPassword());
        emf = Persistence.createEntityManagerFactory("rotinamais", props);
    }

    private EntityManagerUtil() {
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
