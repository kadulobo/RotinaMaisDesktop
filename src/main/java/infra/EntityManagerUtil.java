// path: src/main/java/infra/EntityManagerUtil.java
package infra;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("rotinamais");

    private EntityManagerUtil() {
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
