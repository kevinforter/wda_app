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

    @Override
    public City findCityByName(String cityName) {

        EntityManager em = JpaUtil.createEntityManager();

        City objFromDb = null;

        TypedQuery<City> tQry = em.createQuery("SELECT o FROM City" + " o WHERE o.name = :name", City.class);
        tQry.setParameter("name", cityName);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entity found in the database
            LOG.info("No Weather found for City: " + cityName);
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
        try (EntityManager em = JpaUtil.createEntityManager()) {
            em.getTransaction().begin();

            // Get all city names from the database
            TypedQuery<String> tQry = em.createQuery("SELECT c.name FROM City c", String.class);
            Set<String> existingNames = new HashSet<>(tQry.getResultList());

            for (City city : cityMap.values()) {
                // Check if the city is already in the database
                if (!existingNames.contains(city.getName())) {
                    em.persist(city);
                }
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("Error while saving cities", e);
            throw new CityPersistenceException("Error while saving cities", e);
        }
    }
}
