/**
 * Diese Klasse stellt eine konkrete Implementierung der Schnittstelle
 * 'GenericDAO' dar. Die Persistierung wird dabei mithilfe von ORM realisiert.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.persister.DAO.GenericDAO;
import ch.hslu.informatik.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GenericDAOImpl<T> implements GenericDAO<T> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDAOImpl.class);

    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Persists the provided entity into the database.
     * <p>
     * This method creates an EntityManager instance and starts a transaction.
     * It then persists the provided entity into the database and commits the transaction.
     * If an exception occurs during the execution of the method, it rolls back the transaction and rethrows the exception.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     *
     * @param obj the entity to be persisted
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public void speichern(T obj, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

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

    /**
     * Deletes the entity with the provided id from the database.
     * <p>
     * This method creates an EntityManager instance and retrieves the entity with the provided id.
     * If the entity exists, it starts a transaction, removes the entity from the database, and commits the transaction.
     * If an exception occurs during the execution of the method, it rolls back the transaction.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     *
     * @param id the id of the entity to be deleted
     */
    @Override
    public void loeschen(int id, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

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

    /**
     * Updates the provided entity in the database.
     * <p>
     * This method creates an EntityManager instance and checks if the provided entity is not null.
     * If the entity is not null, it starts a transaction, merges the provided entity with the existing one in the database, and commits the transaction.
     * If an exception occurs during the execution of the method, it rolls back the transaction.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     *
     * @param obj the entity to be updated
     */
    @Override
    public void aktualisieren(T obj, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

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

    /**
     * Retrieves the entity with the provided id from the database.
     * <p>
     * This method creates an EntityManager instance and retrieves the entity with the provided id.
     * The EntityManager is closed after the entity is retrieved to ensure that resources are always properly released.
     * The retrieved entity is then returned.
     *
     * @param id the id of the entity to be retrieved
     * @return the entity with the provided id, or null if no such entity exists
     */
    @Override
    public T findById(int id, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        T objFromDb = em.find(entityClass, id);

        em.close();

        return objFromDb;
    }

    /**
     * Retrieves the entity based on the provided field name and value.
     * <p>
     * This method creates an EntityManager instance and constructs a query to retrieve the entity where the field matches the provided value.
     * The query is then executed and the result is stored.
     * If no entity is found, a log message is generated.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     * The retrieved entity is then returned.
     *
     * @param fieldName the name of the field to be matched
     * @param value     the value to be matched against the field
     * @return the entity that matches the field and value, or null if no such entity exists
     */
    @Override
    public T findEntityByFieldAndString(String fieldName, Object value, String persistenceUnitName) {
        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

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
        }
        return objFromDb;
    }

    /**
     * Retrieves all entities from the database.
     * <p>
     * This method creates an EntityManager instance and constructs a query to retrieve all entities.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the entities are retrieved to ensure that resources are always properly released.
     * The retrieved list of entities is then returned. If no entities are found, an empty list is returned.
     *
     * @return a list of all entities, or an empty list if no entities exist
     */
    @Override
    public List<T> alle(String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        TypedQuery<T> tQry = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass);
        List<T> objListe = tQry.getResultList();

        em.close();
        return objListe != null ? objListe : new ArrayList<>();
    }

    /**
     * Checks if the City table exists in the database.
     * <p>
     * This method creates an EntityManager instance and a TypedQuery to count the number of cities in the database.
     * It executes the query and returns true if the count is greater than 0, indicating that the City table exists, otherwise false.
     * If an exception occurs during the execution of the query, it throws a RuntimeException.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     *
     * @return true if the City table exists, otherwise false
     * @throws RuntimeException if an exception occurs during the execution of the query
     */
    @Override
    public boolean ifTableExist(String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        long count;

        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e", Long.class);

        try {
            count = tQry.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        return count > 0;
    }
}
