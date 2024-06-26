/**
 * Helferklasse zur Erzeugung eines EntityManagers.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.persister.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JpaUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JpaUtil.class);

    private JpaUtil() {
    }

    public static EntityManager createEntityManager(String persistenceUnitName) {
        try {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
            return entityManagerFactory.createEntityManager();
        } catch (Exception e) {
            LOG.error("ERROR: ", e);
            throw new RuntimeException(e);
        }
    }
}