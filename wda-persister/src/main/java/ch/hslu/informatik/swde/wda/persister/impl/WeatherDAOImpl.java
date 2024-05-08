package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;

public class WeatherDAOImpl extends GenericDAOImpl<Weather> implements WeatherDAO {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherDAOImpl.class);

    public WeatherDAOImpl() {
        super(Weather.class);
    }

    @Override
    public long getNumberOfWeatherByCity(String cityName) {

        EntityManager em = JpaUtil.createEntityManager();

        long count;

        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(w) FROM Weather w WHERE w.city.name = :cityName", Long.class);
        tQry.setParameter("cityName", cityName);

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
    public Weather findLatestWeatherByCity(int cityId) {

        EntityManager em = JpaUtil.createEntityManager();

        Weather objFromDb = null;

        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
                " w WHERE w.cityId = :cityId AND w.DTstamp = (SELECT MAX(w.DTstamp) FROM Weather" +
                " w WHERE w.cityId = :cityId)", Weather.class);

        tQry.setParameter("cityId", cityId);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entities found in the database
            LOG.info("No Weather found for City ID: " + cityId);
        }
        em.close();
        return objFromDb;
    }

    // https://chat.openai.com/share/5271fd85-cbcf-4fbc-9e5e-f28880cc1cc3


    @Override
    public Weather findOldestWeatherByCity(int cityId) {

        EntityManager em = JpaUtil.createEntityManager();

        Weather objFromDb = null;

        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
                " w WHERE w.cityId = :cityId AND w.DTstamp = (SELECT MIN(w.DTstamp) FROM Weather" +
                " w WHERE w.cityId = :cityId)", Weather.class);

        tQry.setParameter("cityId", cityId);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entities found in the database
            LOG.info("No Weather found for City ID: " + cityId);
        }
        em.close();
        return objFromDb;
    }

    @Override
    public Weather findWeatherFromCityByDateTime(LocalDateTime DTstamp, int cityId) {

        EntityManager em = JpaUtil.createEntityManager();

        Weather objFromDb = null;

        TypedQuery<Weather> tQry = em.createQuery(
                "SELECT w FROM Weather" + " w " +
                        "WHERE w.cityId = :cityId AND w.DTstamp = :DTstamp"
                , Weather.class);

        tQry.setParameter("DTstamp", DTstamp);
        tQry.setParameter("cityId", cityId);

        try {
            objFromDb = tQry.getSingleResult();
        } catch (Exception e) {
            // No entities found in the database
            LOG.info("No Weather found for City ID: " + cityId);
        }
        em.close();
        return objFromDb;
    }

    @Override
    public List<LocalDateTime> findWeatherDateFromCityByYear(int year, int cityId) {

        EntityManager em = JpaUtil.createEntityManager();

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

    @Override
    public List<Weather> findWeatherFromCityByTimeSpan(int cityId, LocalDateTime von, LocalDateTime bis) {

        EntityManager em = JpaUtil.createEntityManager();

        TypedQuery<Weather> tQry = em.createQuery(
                "SELECT w FROM Weather" + " w " +
                        "WHERE w.cityId = :cityId " +
                        "AND w.DTstamp BETWEEN :von AND :bis", Weather.class);

        tQry.setParameter("cityId", cityId);
        tQry.setParameter("von", von);
        tQry.setParameter("bis", bis);

        List<Weather> objListe = tQry.getResultList();

        em.close();

        return objListe != null ? objListe : new ArrayList<>();
    }

    @Override
    public List<Weather> findMinMaxTemperatureByDateTime(LocalDateTime DTstamp) {

        EntityManager em = JpaUtil.createEntityManager();

        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp AND w.currTempCelsius = (SELECT MAX(w.currTempCelsius) FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp) " +
                "UNION " +
                "SELECT w FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp AND w.currTempCelsius = (SELECT MIN(w.currTempCelsius) FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp)", Weather.class);

        tQry.setParameter("DTstamp", DTstamp);

        List<Weather> objListe = tQry.getResultList();

        em.close();

        return objListe != null ? objListe : new ArrayList<>();
    }

    @Override
    public List<Weather> findMinMaxHumidityByDateTime(LocalDateTime DTstamp) {

        EntityManager em = JpaUtil.createEntityManager();

        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp AND w.humidity = (SELECT MAX(w.humidity) FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp)" +
                "UNION " +
                "SELECT w FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp AND w.humidity = (SELECT MIN(w.humidity) FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp)", Weather.class);

        tQry.setParameter("DTstamp", DTstamp);

        List<Weather> objListe = tQry.getResultList();

        em.close();

        return objListe != null ? objListe : new ArrayList<>();
    }

    @Override
    public List<Weather> findMinMaxPressureByDateTime(LocalDateTime DTstamp) {

        EntityManager em = JpaUtil.createEntityManager();

        TypedQuery<Weather> tQry = em.createQuery("SELECT w FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp AND w.pressure = (SELECT MAX(w.pressure) FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp)" +
                "UNION " +
                "SELECT w FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp AND w.pressure = (SELECT MIN(w.pressure) FROM Weather" +
                " w WHERE w.DTstamp = :DTstamp)", Weather.class);

        tQry.setParameter("DTstamp", DTstamp);

        List<Weather> objListe = tQry.getResultList();

        em.close();

        return objListe != null ? objListe : new ArrayList<>();
    }

    @Override
    public void saveAllWeather(HashMap<LocalDateTime, Weather> weatherMap, String cityName) {

        EntityManager em = JpaUtil.createEntityManager();

        try {
            em.getTransaction().begin();

            // Erstellen Sie ein Set mit den vorhandenen Wetterdaten
            TypedQuery<LocalDateTime> tQry = em.createQuery("SELECT w.DTstamp FROM Weather w WHERE w.city.name = :cityName", LocalDateTime.class);
            tQry.setParameter("cityName", cityName);

            Set<LocalDateTime> existingWeatherDates = new HashSet<>(tQry.getResultList());

            // Speichern Sie die neuen Wetterdaten in Batches
            int i = 0;
            for (Weather weather : weatherMap.values()) {
                // Überprüfen Sie, ob das Wetter bereits existiert
                if (!existingWeatherDates.contains(weather.getDTstamp())) {
                    em.persist(weather);
                    i++;
                    // Flushen und leeren Sie den EntityManager alle 50 Wetterdaten
                    if (i % 50 == 0) {
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
            LOG.info(e.getMessage());
        } finally {
            em.close();
        }
    }

    @Override
    public boolean ifWeatherOfCityExist(String cityName) {

        EntityManager em = JpaUtil.createEntityManager();

        long count;

        TypedQuery<Long> tQry = em.createQuery("SELECT COUNT(w) FROM Weather w WHERE w.city.name = :cityName", Long.class);
        tQry.setParameter("cityName", cityName);

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
