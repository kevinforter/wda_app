/**
 * Diese Klasse stellt eine konkrete Implementierung der Schnittstelle
 * 'WeatherDAO' dar.
 * Die Persistierung wird dabei mithilfe von ORM realisiert.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.eclipse.persistence.internal.jpa.EntityManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.*;
import java.util.*;

public class WeatherDAOImpl extends GenericDAOImpl<Weather> implements WeatherDAO {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherDAOImpl.class);

    private String persistenceUnitName;

    public WeatherDAOImpl() {
        super(Weather.class);
    }

    public WeatherDAOImpl(String persistenceUnitName) {
        super(Weather.class, persistenceUnitName);
        this.persistenceUnitName = persistenceUnitName;
    }

    /**
     * Retrieves the count of Weather entities associated with a specific city.
     * <p>
     * This method creates an EntityManager instance and constructs a query to count all Weather entities
     * associated with the provided city name.
     * The query is then executed and the result is stored in a long variable.
     * The EntityManager is closed after the count is retrieved to ensure that resources are always properly released.
     * The retrieved count of Weather entities is then returned.
     * If an exception occurs during the execution of the query,
     * a RuntimeException is thrown.
     *
     * @param cityId the id of the city for which the count of Weather entities is to be retrieved
     * @return the count of Weather entities associated with the provided city name
     * @throws RuntimeException if an exception occurs during the execution of the query
     */
    @Override
    public long getNumberOfWeatherByCity(int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        long count;

        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(w) FROM Weather w WHERE w.city.id = :cityId", Long.class);
        tQry.setParameter("cityId", cityId);

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
     * Retrieves the latest Weather entity associated with a specific city.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entity
     * associated with the provided city ID that has the latest timestamp.
     * The query is then executed and the result is stored.
     * The EntityManager is closed
     * after the Weather entity is retrieved to ensure that resources are always properly released.
     * The retrieved Weather entity is then returned.
     * If no entity is found during the execution of the query,
     * a log message is generated.
     *
     * @param cityId the ID of the city for which the latest Weather entity is to be retrieved
     * @return the latest Weather entity associated with the provided city ID, or null if no entity is found
     */
    @Override
    public Weather findLatestWeatherByCity(int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        Weather objFromDb = null;

        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
                " w WHERE w.cityId = :cityId AND w.DTstamp = (SELECT MAX(w.DTstamp) FROM Weather" +
                " w WHERE w.cityId = :cityId)", Weather.class);

        tQry.setParameter("cityId", cityId);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entities found in the database
            LOG.debug("No Weather found for City ID: " + cityId);
        }
        em.close();
        return objFromDb;
    }

    /**
     * Retrieves the oldest Weather entity associated with a specific city.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entity
     * associated with the provided city ID that has the oldest timestamp.
     * The query is then executed and the result is stored.
     * The EntityManager is closed
     * after the Weather entity is retrieved to ensure that resources are always properly released.
     * The retrieved Weather entity is then returned.
     * If no entity is found during the execution of the query,
     * a log message is generated.
     *
     * @param cityId the ID of the city for which the oldest Weather entity is to be retrieved
     * @return the oldest Weather entity associated with the provided city ID, or null if no entity is found
     */
    @Override
    public Weather findOldestWeatherByCity(int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        Weather objFromDb = null;

        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
                " w WHERE w.cityId = :cityId AND w.DTstamp = (SELECT MIN(w.DTstamp) FROM Weather" +
                " w WHERE w.cityId = :cityId)", Weather.class);

        tQry.setParameter("cityId", cityId);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entities found in the database
            LOG.debug("No Weather found for City ID: " + cityId);
        }
        em.close();
        return objFromDb;
    }

    /**
     * Retrieves a Weather entity associated with a specific city and timestamp.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entity
     * associated with the provided city ID and timestamp.
     * The query is then executed and the result is stored.
     * The EntityManager is closed
     * after the Weather entity is retrieved to ensure that resources are always properly released.
     * The retrieved Weather entity is then returned.
     * If no entity is found during the execution of the query,
     * a log message is generated.
     *
     * @param DTstamp the timestamp for which the Weather entity is to be retrieved
     * @param cityId  the ID of the city for which the Weather entity is to be retrieved
     * @return the Weather entity associated with the provided city ID and timestamp, or null if no entity is found
     */
    @Override
    public Weather findWeatherFromCityByDateTime(LocalDateTime DTstamp, int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        TypedQuery<Weather> tQry = em.createQuery(
                "SELECT w FROM Weather w WHERE w.cityId = :cityId ORDER BY w.DTstamp ASC"
                , Weather.class
        );
        tQry.setParameter("cityId", cityId);
        tQry.setMaxResults(1);

        List<Weather> results = tQry.getResultList();

        Weather closestWeather = null;
        long closestDifference = Long.MAX_VALUE;

        for (Weather weather : results) {
            long difference = Math.abs(Duration.between(weather.getDTstamp(), DTstamp).getSeconds());
            if (difference < closestDifference) {
                closestDifference = difference;
                closestWeather = weather;
            }
        }

        return closestWeather;
    }

    /**
     * Retrieves a list of timestamps for Weather entities associated with a specific city and year.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the timestamps of Weather entities
     * associated with the provided city ID and year.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the list is retrieved to ensure that resources are always properly released.
     * The retrieved list of timestamps is then returned.
     * If no timestamps are found during the execution of the query,
     * an empty list is returned.
     *
     * @param year   the year for which the timestamps are to be retrieved
     * @param cityId the ID of the city for which the timestamps are to be retrieved
     * @return a list of timestamps associated with the provided city ID and year, or an empty list if no timestamps are found
     */
    @Override
    public List<LocalDateTime> findWeatherDateFromCityByYear(int year, int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        LocalDateTime DTstamp = LocalDateTime.of(year, 1, 1, 0, 0, 0);

        TypedQuery<LocalDateTime> tQry = em.createQuery("SELECT w.DTstamp FROM Weather" + " w " +
                        "WHERE w.cityId = :cityId AND w.DTstamp >= :DTstamp"
                , LocalDateTime.class);

        tQry.setParameter("DTstamp", DTstamp);
        tQry.setParameter("cityId", cityId);

        List<LocalDateTime> objListe = tQry.getResultList();

        em.close();

        return objListe != null ? objListe : new ArrayList<>();
    }

    /**
     * Retrieves a map of Weather entities associated with a specific city and year.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entities
     * associated with the provided city ID and year.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the list is retrieved to ensure that resources are always properly released.
     * The retrieved list of Weather entities is then converted into a TreeMap
     * where the key is the timestamp and the value is the Weather entity.
     * The TreeMap is sorted in ascending order of the timestamp.
     * The TreeMap is then returned.
     *
     * @param year   the year for which the Weather entities are to be retrieved
     * @param cityId the ID of the city for which the Weather entities are to be retrieved
     * @return a TreeMap of Weather entities associated with the provided city ID and year, sorted in ascending order of the timestamp
     */
    @Override
    public TreeMap<LocalDateTime, Weather> findWeatherFromCityByYear(int year, int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);

        TypedQuery<Weather> query = em.createQuery(
                "SELECT w FROM Weather w WHERE w.cityId = :cityId AND w.DTstamp >= :startOfYear",
                Weather.class
        );

        query.setParameter("startOfYear", startOfYear);
        query.setParameter("cityId", cityId);

        List<Weather> weatherList = query.getResultList();
        em.close();

        TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();

        for (Weather w : weatherList) {
            weatherMap.put(w.getDTstamp(), w);
        }

        return weatherMap != null ? weatherMap : new TreeMap<>();
    }

    /**
     * Retrieves a map of Weather entities associated with a year.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entities
     * associated with the provided year.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the list is retrieved to ensure that resources are always properly released.
     * The retrieved list of Weather entities is then converted into a TreeMap
     * where the key is the timestamp and the value is the Weather entity.
     * The TreeMap is sorted in ascending order of the timestamp.
     * The TreeMap is then returned.
     *
     * @param year the year for which the Weather entities are to be retrieved
     * @return a TreeMap of Weather entities associated with the provided city ID and year, sorted in ascending order of the timestamp
     */
    @Override
    public TreeMap<LocalDateTime, Weather> findWeatherByYear(int year) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        LocalDateTime startOfYear = LocalDateTime.of(year, 1, 1, 0, 0, 0);

        TypedQuery<Weather> query = em.createQuery(
                "SELECT w FROM Weather w WHERE w.DTstamp >= :startOfYear",
                Weather.class
        );

        query.setParameter("startOfYear", startOfYear);

        List<Weather> weatherList = query.getResultList();
        em.close();

        TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();

        for (Weather w : weatherList) {
            weatherMap.put(w.getDTstamp(), w);
        }

        return weatherMap != null ? weatherMap : new TreeMap<>();
    }

    /**
     * Retrieves a map of Weather entities associated with a month.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entities
     * associated with the provided year.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the list is retrieved to ensure that resources are always properly released.
     * The retrieved list of Weather entities is then converted into a TreeMap
     * where the key is the timestamp and the value is the Weather entity.
     * The TreeMap is sorted in ascending order of the timestamp.
     * The TreeMap is then returned.
     *
     * @param month the year for which the Weather entities are to be retrieved
     * @return a TreeMap of Weather entities associated with the provided city ID and year, sorted in ascending order of the timestamp
     */
    @Override
    public TreeMap<LocalDateTime, Weather> findWeatherFromCityByMonth(int month, int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        int yearNow = Year.now().getValue();
        LocalDateTime startOfMonth = LocalDateTime.of(yearNow, month, 1, 0, 0);

        int lastDayOfMonth = YearMonth.of(yearNow, month).atEndOfMonth().getDayOfMonth();
        LocalDateTime endOfMonth = LocalDateTime.of(Year.now().getValue(), month, lastDayOfMonth, 0, 0);

        TypedQuery<Weather> query = em.createQuery(
                "SELECT w FROM Weather w WHERE w.cityId = :cityId AND w.DTstamp BETWEEN :startOfMonth AND :endOfMonth",
                Weather.class
        );

        query.setParameter("startOfMonth", startOfMonth);
        query.setParameter("endOfMonth", endOfMonth);
        query.setParameter("cityId", cityId);

        List<Weather> weatherList = query.getResultList();
        em.close();

        TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();

        for (Weather w : weatherList) {
            weatherMap.put(w.getDTstamp(), w);
        }

        return weatherMap != null ? weatherMap : new TreeMap<>();
    }

    /**
     * Retrieves a map of Weather entities associated with a month.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entities
     * associated with the provided year.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the list is retrieved to ensure that resources are always properly released.
     * The retrieved list of Weather entities is then converted into a TreeMap
     * where the key is the timestamp and the value is the Weather entity.
     * The TreeMap is sorted in ascending order of the timestamp.
     * The TreeMap is then returned.
     *
     * @param week the year for which the Weather entities are to be retrieved
     * @return a TreeMap of Weather entities associated with the provided city ID and year, sorted in ascending order of the timestamp
     */
    @Override
    public TreeMap<LocalDateTime, Weather> findWeatherFromCityByWeek(int week, int cityId) {
        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        // Calculate the start and end dates of the week
        LocalDate startOfWeek = LocalDate.of(Year.now().getValue(), 1, 1).plusWeeks(week - 1);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // Convert LocalDate to LocalDateTime at start and end of day
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = endOfWeek.atTime(23, 59, 59);

        // Query the weather data from the database
        TypedQuery<Weather> query = em.createQuery(
                "SELECT w FROM Weather w WHERE w.cityId = :cityId AND w.DTstamp BETWEEN :startOfWeek AND :endOfWeek",
                Weather.class
        );

        query.setParameter("startOfWeek", startDateTime);
        query.setParameter("endOfWeek", endDateTime);
        query.setParameter("cityId", cityId);

        List<Weather> weatherList = query.getResultList();
        em.close();

        TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();

        for (Weather w : weatherList) {
            weatherMap.put(w.getDTstamp(), w);
        }

        return weatherMap != null ? weatherMap : new TreeMap<>();
    }

    /**
     * Retrieves a map of Weather entities within a specific number of days from the current date.
     * <p>
     * This method creates an EntityManager instance and calculates the date difference from the current date.
     * It then constructs a query
     * to find the Weather entities that have a timestamp greater than or equal to the calculated date.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the list is retrieved to ensure that resources are always properly released.
     * The retrieved list of Weather entities is then converted into a TreeMap
     * where the key is the timestamp and the value is the Weather entity.
     * The TreeMap is sorted in ascending order of the timestamp.
     * The TreeMap is then returned.
     *
     * @param days the number of days from the current date for which the Weather entities are to be retrieved
     * @return a TreeMap of Weather entities within the specified number of days from the current date, sorted in ascending order of the timestamp
     */
    @Override
    public TreeMap<LocalDateTime, Weather> findWeatherByDayDifference(int days, int cityId) {

        // Create an EntityManager instance
        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        // Calculate the date difference from the current date
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime difference = today.minusDays(days);

        // Construct a query to find the Weather entities that have a timestamp greater than or equal to the calculated date
        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather w WHERE w.city.id = :cityId AND w.DTstamp >= :difference", Weather.class);
        tQry.setParameter("cityId", cityId);
        tQry.setParameter("difference", difference);

        // Execute the query and store the result in a list
        List<Weather> weatherList = tQry.getResultList();

        // Close the EntityManager to ensure that resources are always properly released
        em.close();

        // Initialize a TreeMap to store the Weather entities
        TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();

        // Convert the list of Weather entities into a TreeMap where the key is the timestamp and the value is the Weather entity
        for (Weather w : weatherList) {
            weatherMap.put(w.getDTstamp(), w);
        }

        // Return the TreeMap of Weather entities
        return weatherMap != null ? weatherMap : new TreeMap<>();
    }

    /**
     * Retrieves a map of Weather entities associated with a specific city and within a specific time span.
     * <p>
     * This method creates an EntityManager instance and constructs a query to find the Weather entities
     * associated with the provided city ID and within the time span defined by the 'von' and 'bis' parameters.
     * The query is then executed and the result is stored in a list.
     * The EntityManager is closed after the list is retrieved to ensure that resources are always properly released.
     * The retrieved list of Weather entities is then converted into a TreeMap
     * where the key is the timestamp and the value is the Weather entity.
     * The TreeMap is sorted in ascending order of the timestamp.
     * The TreeMap is then returned.
     *
     * @param cityId the ID of the city for which the Weather entities are to be retrieved
     * @param von    the start of the time span for which the Weather entities are to be retrieved
     * @param bis    the end of the time span for which the Weather entities are to be retrieved
     * @return a TreeMap of Weather entities associated with the provided city ID and within the specified time span, sorted in ascending order of the timestamp
     */
    @Override
    public TreeMap<LocalDateTime, Weather> findWeatherFromCityByTimeSpan(int cityId, LocalDateTime von, LocalDateTime bis) {

        // Create an EntityManager instance
        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        // Construct a query to find the Weather entities associated with the provided city ID and within the specified time span
        TypedQuery<Weather> tQry = em.createQuery(
                "SELECT w FROM Weather" + " w " +
                        "WHERE w.cityId = :cityId " +
                        "AND w.DTstamp BETWEEN :von AND :bis", Weather.class);

        // Set the parameters for the query
        tQry.setParameter("cityId", cityId);
        tQry.setParameter("von", von);
        tQry.setParameter("bis", bis);

        // Execute the query and store the result in a list
        List<Weather> weatherList = tQry.getResultList();

        // Close the EntityManager to ensure that resources are always properly released
        em.close();

        // Initialize a TreeMap to store the Weather entities
        TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();

        // Convert the list of Weather entities into a TreeMap where the key is the timestamp and the value is the Weather entity
        for (Weather w : weatherList) {
            weatherMap.put(w.getDTstamp(), w);
        }

        // Return the TreeMap of Weather entities
        return weatherMap != null ? weatherMap : new TreeMap<>();
    }
//
//    @Override
//    public List<Weather> findMinMaxTemperatureByDateTime(LocalDateTime DTstamp) {
//
//        EntityManager em = JpaUtil.createEntityManager();
//
//        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp AND w.currTempCelsius = (SELECT MAX(w.currTempCelsius) FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp) " +
//                "UNION " +
//                "SELECT w FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp AND w.currTempCelsius = (SELECT MIN(w.currTempCelsius) FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp)", Weather.class);
//
//        tQry.setParameter("DTstamp", DTstamp);
//
//        List<Weather> objListe = tQry.getResultList();
//
//        em.close();
//
//        return objListe != null ? objListe : new ArrayList<>();
//    }
//
//    @Override
//    public List<Weather> findMinMaxHumidityByDateTime(LocalDateTime DTstamp) {
//
//        EntityManager em = JpaUtil.createEntityManager();
//
//        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp AND w.humidity = (SELECT MAX(w.humidity) FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp)" +
//                "UNION " +
//                "SELECT w FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp AND w.humidity = (SELECT MIN(w.humidity) FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp)", Weather.class);
//
//        tQry.setParameter("DTstamp", DTstamp);
//
//        List<Weather> objListe = tQry.getResultList();
//
//        em.close();
//
//        return objListe != null ? objListe : new ArrayList<>();
//    }
//
//    @Override
//    public List<Weather> findMinMaxPressureByDateTime(LocalDateTime DTstamp) {
//
//        EntityManager em = JpaUtil.createEntityManager();
//
//        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp AND w.pressure = (SELECT MAX(w.pressure) FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp)" +
//                "UNION " +
//                "SELECT w FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp AND w.pressure = (SELECT MIN(w.pressure) FROM Weather" +
//                " w WHERE w.DTstamp = :DTstamp)", Weather.class);
//
//        tQry.setParameter("DTstamp", DTstamp);
//
//        List<Weather> objListe = tQry.getResultList();
//
//        em.close();
//
//        return objListe != null ? objListe : new ArrayList<>();
//    }

    /**
     * Saves all Weather entities from a provided map into the database.
     * <p>
     * This method creates an EntityManager instance and begins a transaction.
     * It then constructs a query to retrieve all existing Weather entities associated with the provided city name.
     * The result is stored in a Set of LocalDateTime instances.
     * The method then iterates over the provided map of Weather entities.
     * For each Weather entity, it checks if the entity already exists in the database.
     * If the entity does not exist, it is persisted into the database.
     * Every 50 entities, the EntityManager is flushed and cleared to manage memory.
     * After all entities have been processed, the transaction is committed.
     * If an exception occurs during the execution of the method, the transaction is rolled back and a log message is generated.
     * The EntityManager is closed after the operation is completed.
     *
     * @param weatherMap a TreeMap of Weather entities to be saved, where the key is the timestamp and the value is the Weather entity
     */
    @Override
    public void saveAllWeather(TreeMap<LocalDateTime, Weather> weatherMap) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        try {
            em.getTransaction().begin();

            int i = 0;
            for (Weather weather : weatherMap.values()) {

                em.persist(weather);
                i++;
                if (i % 50 == 0) {
                    em.flush();
                    em.clear();
                }

            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            LOG.debug(e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * Checks if any Weather entities associated with a specific city exist in the database.
     * <p>
     * This method creates an EntityManager instance and constructs a query to count all Weather entities
     * associated with the provided city name.
     * The query is then executed and the result is stored in a long variable.
     * The EntityManager is closed after the count is retrieved to ensure that resources are always properly released.
     * The method then returns a boolean indicating whether any Weather entities associated with the city exist in the database.
     * If an exception occurs during the execution of the query, a RuntimeException is thrown.
     *
     * @param cityId the id of the city for which to check the existence of associated Weather entities
     * @return true if any Weather entities associated with the city exist in the database, false otherwise
     * @throws RuntimeException if an exception occurs during the execution of the query
     */
    @Override
    public boolean ifWeatherOfCityExist(int cityId) {

        EntityManager em = JpaUtil.createEntityManager(persistenceUnitName);

        long count;

        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(w) FROM Weather w WHERE w.city.id = :cityId", Long.class);
        tQry.setParameter("cityId", cityId);

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
