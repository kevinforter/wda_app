package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.exception.CityPersistenceException;
import ch.hslu.informatik.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

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
            LOG.error("City not found: " + cityName, e);
            throw new CityPersistenceException("City not found: " + cityName, e);
        }

        return objFromDb;
    }

    @Override
    public void saveAllCities(LinkedHashMap<Integer, City> cityMap) {
        try (EntityManager em = JpaUtil.createEntityManager()) {
            em.getTransaction().begin();

            // Get all city names from the database
            TypedQuery<String> nameQuery = em.createQuery("SELECT c.name FROM City c", String.class);
            Set<String> existingNames = new HashSet<>(nameQuery.getResultList());

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
