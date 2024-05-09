package ch.hslu.informatik.swde.wda.business.util;

import ch.hslu.informatik.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;

public class Util {

    private Util() {

    }

    public static void cleanDatabase() {

        EntityManager em = JpaUtil.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Weather e").executeUpdate();
        em.createQuery("DELETE FROM City e").executeUpdate();

        em.getTransaction().commit();

        em.close();

    }
}