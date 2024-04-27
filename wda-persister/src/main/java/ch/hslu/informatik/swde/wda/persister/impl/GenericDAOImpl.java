package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.persister.DAO.GenericDAO;
import ch.hslu.informatik.swde.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse stellt eine konkrete Implementierung der Schnittstelle
 * 'GenericDAO' dar. Die Persistierung wird dabei mithilfe von ORM realisiert.
 */

public class GenericDAOImpl<T> implements GenericDAO<T> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDAOImpl.class);

    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public void speichern(T obj) {

        EntityManager em = JpaUtil.createEntityManager();

        try {
            em.getTransaction().begin();
            em.persist(obj);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (em.isOpen()) {
                em.close();
            }
        }

    }

    @Override
    public void loeschen(int id) {

        EntityManager em = JpaUtil.createEntityManager();

        T objToDelete = em.find(entityClass, id);

        if (objToDelete != null) {

            try {
                em.getTransaction().begin();
                em.remove(objToDelete);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }

    }

    @Override
    public void aktualisieren(T obj) {

        EntityManager em = JpaUtil.createEntityManager();

        if (obj != null) {

            try {
                em.getTransaction().begin();
                em.merge(obj);
                em.getTransaction().commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } finally {
                if (em.isOpen()) {
                    em.close();
                }
            }
        }

    }

    @Override
    public T findById(int id) {

        EntityManager em = JpaUtil.createEntityManager();

        T objFromDb = em.find(entityClass, id);

        em.close();

        return objFromDb;
    }

    @Override
    public T findEntityByFieldAndString(String fieldName, String value) {
        EntityManager em = JpaUtil.createEntityManager();

        T objFromDb = null;

        TypedQuery<T> tQry = em.createQuery("SELECT o FROM " + entityClass.getSimpleName() + " o WHERE o." + fieldName + " = :value", entityClass);
        tQry.setParameter("value", value);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entity found in the database
            LOG.info("No Entity found for field: " + fieldName + " and value: " + value);
        } finally {
            em.close();
            return objFromDb;
        }
    }

    @Override
    public List<T> alle() {

        EntityManager em = JpaUtil.createEntityManager();

        TypedQuery<T> tQry = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
        List<T> objListe = tQry.getResultList();

        em.close();
        return objListe != null ? objListe : new ArrayList<>();
    }


}
