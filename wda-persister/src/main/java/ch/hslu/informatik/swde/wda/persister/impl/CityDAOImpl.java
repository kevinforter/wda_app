/**
 * Diese Schnittstelle ergänzt die generische Persister-Schnittstelle
 * mit zusätzlichen Funktionalitäten für die Persistierung von Ortschaften.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.exception.CityPersistenceException;
import ch.hslu.informatik.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CityDAOImpl extends GenericDAOImpl<City> implements CityDAO {

    private static final Logger LOG = LoggerFactory.getLogger(CityDAOImpl.class);

    public CityDAOImpl() {
        super(City.class);
    }

    /**
     * Retrieves the total number of cities in the database.
     * <p>
     * This method creates an EntityManager instance and a TypedQuery to count the number of cities in the database.
     * It executes the query and returns the count.
     * If an exception occurs during the execution of the query, it throws a RuntimeException.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     *
     * @return the total number of cities in the database
     * @throws RuntimeException if an exception occurs during the execution of the query
     */
    @Override
    public long getNumberOfCities(String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        long count;

        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(c) FROM City c", Long.class);

        try {
            count = tQry.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
        return count;
    }

    /**
     * Retrieves the ID of a city by its name.
     * <p>
     * This method creates an EntityManager instance and a TypedQuery to find the ID of a city by its name in the database.
     * It executes the query and returns the ID.
     * If an exception occurs during the execution of the query, it logs an info message and returns 0.
     * The EntityManager is closed after the execution of the query.
     *
     * @param cityName the name of the city for which the ID is to be retrieved
     * @return the ID of the city if found, otherwise 0
     */
    @Override
    public int findCityIdByName(String cityName, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        int cityId = 0;

        TypedQuery<Integer> tQry = em.createQuery("SELECT c.id FROM City c WHERE c.name = :name", Integer.class);
        tQry.setParameter("name", cityName);

        try {
            cityId = tQry.getSingleResult();
        } catch (Exception e) {
            // No entity found in the database
            LOG.debug("No found for City: " + cityName);
        }
        em.close();
        return cityId;
    }

    /**
     * Retrieves a city by its name.
     * <p>
     * This method creates an EntityManager instance and a TypedQuery to find a city by its name in the database.
     * It executes the query and returns the city.
     * If an exception occurs during the execution of the query, it logs an info message and returns null.
     * The EntityManager is closed after the execution of the query.
     *
     * @param cityName the name of the city to be retrieved
     * @return the city if found, otherwise null
     */
    @Override
    public City findCityByName(String cityName, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        City objFromDb = null;

        TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c WHERE c.name = :name", City.class);
        tQry.setParameter("name", cityName);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entity found in the database
            LOG.debug("No found for City: " + cityName);
        }
        em.close();
        return objFromDb;
    }


    /**
     * Checks if a city exists in the database by its name.
     * <p>
     * This method creates an EntityManager instance and a TypedQuery to count the number of cities with the given name in the database.
     * It executes the query and returns true if the count is greater than 0, otherwise false.
     * If an exception occurs during the execution of the query, it logs an error message.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     *
     * @param cityName the name of the city to be checked
     * @return true if the city exists, otherwise false
     */
    @Override
    public boolean cityExists(String cityName, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        // Create a query to count the number of cities with the given name
        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(c) FROM City c WHERE c.name = :name", Long.class);
        tQry.setParameter("name", cityName);

        long count = 0;
        try {
            // Execute the query
            count = tQry.getSingleResult();
        } catch (Exception e) {
            LOG.error("Error while checking if city exists: " + cityName, e);
        } finally {
            em.close();
        }

        // If the count is greater than 0, the city exists
        return count > 0;
    }

    /**
     * Retrieves all city names in the database.
     * <p>
     * This method creates an EntityManager instance and a TypedQuery to find all city names in the database.
     * It executes the query and returns the list of city names.
     * The EntityManager is closed after the execution of the query.
     *
     * @return a list of all city names in the database, or an empty list if no city names are found
     */
    @Override
    public Set<String> allCityNames(String persistenceUnitName) {
        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        Set<String> existingNames = new HashSet<>(em.createQuery("SELECT c.name FROM City c", String.class).getResultList());
        /*
        TypedQuery<String> tQry = em.createQuery("SELECT c.name FROM City c", String.class);
        List<String> objListe = tQry.getResultList();

         */

        em.close();
        return existingNames;
    }

    /**
     * Saves all cities from the provided map into the database.
     * <p>
     * This method creates an EntityManager instance and starts a transaction.
     * It retrieves all existing city names from the database and stores them in a set for quick lookup.
     * Then it iterates over the provided map of cities. If a city's name is not in the set of existing names,
     * it persists the city into the database.
     * To optimize performance, the EntityManager is flushed and cleared every 10 iterations.
     * After all cities have been processed, the transaction is committed.
     * If an exception occurs during the execution of the method, it rolls back the transaction, logs an error message,
     * and throws a CityPersistenceException.
     * The EntityManager is closed in the "finally" block to ensure that resources are always properly released.
     *
     * @param cityMap a map of cities to be saved into the database
     * @throws CityPersistenceException if an exception occurs during the execution of the method
     */
    @Override
    public void saveAllCities(LinkedHashMap<Integer, City> cityMap, String persistenceUnitName) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        try {
            em.getTransaction().begin();

            int i = 0;
            for (City city : cityMap.values()) {
                em.persist(city);
                i++;
                if (i % 10 == 0) {
                    em.flush();
                    em.clear();
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.error("Error while saving cities", e);
            throw new CityPersistenceException("Error while saving cities", e);
        } finally {
            em.close();
        }
    }
}
