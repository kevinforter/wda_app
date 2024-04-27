package ch.hslu.informatik.swde.wda.persister.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helferklasse zur Erzeugung eines EntityManagers.
 */

public class JpaUtil {

    private JpaUtil() {

    }

    private static final Logger LOG = LoggerFactory.getLogger(JpaUtil.class);

    private static final EntityManagerFactory entityManagerFactory;

    static {
        try {
            /* EntityManagerFactory erzeugen */
            entityManagerFactory = Persistence.createEntityManagerFactory("postgresPU");
        } catch (Exception e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }
    }

    public static EntityManager createEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
