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

    /**
     * Constructor for the CityDAOImpl class.
     * <p>
     * This constructor calls the superclass constructor with the City class as a parameter.
     * This is used to set the type of the entity for the generic DAO implementation.
     */
    public CityDAOImpl() {
        super(City.class);
    }

    @Override
    public long getNumberOfCities() {

        EntityManager em = JpaUtil.createEntityManager();

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

    @Override
    public int findCityIdByName(String cityName) {

        EntityManager em = JpaUtil.createEntityManager();

        int cityId = 0;

        TypedQuery<Integer> tQry = em.createQuery("SELECT c.id FROM City c WHERE c.name = :name", Integer.class);
        tQry.setParameter("name", cityName);

        try {
            cityId = tQry.getSingleResult();
        } catch (Exception e) {
            // No entity found in the database
            LOG.info("No found for City: " + cityName);
        }
        em.close();
        return cityId;
    }

    @Override
    public City findCityByName(String cityName) {

        EntityManager em = JpaUtil.createEntityManager();

        City objFromDb = null;

        TypedQuery<City> tQry = em.createQuery("SELECT c FROM City c WHERE c.name = :name", City.class);
        tQry.setParameter("name", cityName);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entity found in the database
            LOG.info("No found for City: " + cityName);
        }
        em.close();
        return objFromDb;
    }


    @Override
    public boolean cityExists(String cityName) {

        EntityManager em = JpaUtil.createEntityManager();

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

        // If count is greater than 0, the city exists
        return count > 0;
    }

    @Override
    public List<String> allCityNames() {
        EntityManager em = JpaUtil.createEntityManager();

        TypedQuery<String> tQry = em.createQuery("SELECT c.name FROM City c", String.class);
        List<String> objListe = tQry.getResultList();

        em.close();
        return objListe != null ? objListe : new ArrayList<>();
    }

    @Override
    public void saveAllCities(LinkedHashMap<Integer, City> cityMap) {

        EntityManager em = JpaUtil.createEntityManager();

        try {
            em.getTransaction().begin();

            // Get all city names from the database
            Set<String> existingNames = new HashSet<>(em.createQuery("SELECT c.name FROM City c", String.class).getResultList());

            int i = 0;
            for (City city : cityMap.values()) {
                // Check if the city is already in the database
                if (!existingNames.contains(city.getName())) {
                    em.persist(city);
                    i++;
                    // Flushen und leeren Sie den EntityManager alle 50 Wetterdaten
                    if (i % 10 == 0) {
                        em.flush();
                        em.clear();
                    }
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

    @Override
    public boolean ifTableExist() {

        EntityManager em = JpaUtil.createEntityManager();

        long count;

        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(c) FROM City c", Long.class);

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
