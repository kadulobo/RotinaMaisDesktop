// path: src/main/java/infra/EntityManagerUtil.java
package infra;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtil {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("rotinamais");

    private EntityManagerUtil() {
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
