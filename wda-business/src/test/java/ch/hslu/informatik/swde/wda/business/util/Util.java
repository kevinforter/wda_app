package ch.hslu.informatik.swde.wda.business.util;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.impl.CityDAOImpl;
import ch.hslu.informatik.swde.wda.persister.impl.WeatherDAOImpl;
import ch.hslu.informatik.swde.wda.persister.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

public class Util {

    private static final CityDAO daoC = new CityDAOImpl();
    private static final WeatherDAO daoW = new WeatherDAOImpl();

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

    public static void saveDummyWeather() {

        City dummyCity = new City(7270, "Davos", "CH");
        daoC.speichern(dummyCity);

        Weather dummyWeather = new Weather(daoC.findCityIdByName("Davos"), LocalDateTime.now().minusDays(1), "DUMMY", "DUMMY", 69.69, 69.69, 69.69, 69.69, 69.69);
        daoW.speichern(dummyWeather);
    }
}