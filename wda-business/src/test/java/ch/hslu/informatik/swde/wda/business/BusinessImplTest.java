package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class BusinessImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessImplTest.class);

    @Tag("integration")
    @Test
    void addAllCities() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        long start = System.currentTimeMillis();
        serviceAPI.addAllCities();
        long end = System.currentTimeMillis();

        long time = end - start;
        System.out.println(time);
        //LOG.info("Time used to save all Cities: " + time + " ms");
    }

    @Tag("integration")
    @Test
    void addCurrentWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCityList = serviceAPI.getAllCities();

        for (City c : resCityList) {

            long start = System.currentTimeMillis();
            serviceAPI.addCurrentWeatherOfCity(c.getName());
            long end = System.currentTimeMillis();

            long time = end - start;
            LOG.info("Time used to save current Weather: " + time + " ms");
        }
    }

    @Tag("integration")
    @Test
    void addWeatherOfCityByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        long start = System.currentTimeMillis();
        serviceAPI.addWeatherOfCityByYear("Davos",2024);
        long end = System.currentTimeMillis();

        long time = end - start;
        LOG.info("Time used to get and save all weather Data: " + time + " ms");

    }

    @Tag("integration")
    @Test
    void addWeatherOfAllCitiesByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {

            long start = System.currentTimeMillis();
            serviceAPI.addWeatherOfCityByYear(c.getName(),2024);
            long end = System.currentTimeMillis();

            long time = end - start;
            LOG.info("Time used to get and save all weather Data: " + time + " ms");
        }
    }

    @Tag("integration")
    @Test
    void getAllCities() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {
            LOG.info("City : " + c);
        }
    }

    @Tag("integration")
    @Test
    void getLatestWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {
            Weather currentWeather = serviceAPI.getLatestWeatherOfCity(c.getName());
            LOG.info("Current Weather: " + currentWeather);
        }
    }

    @Tag("integration")
    @Test
    void getWeatherOfCityByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        serviceAPI.addAllCities();

        List<City> cityList = serviceAPI.getAllCities();

        for (City c : cityList) {
            serviceAPI.addCurrentWeatherOfCity(c.getName());
            TreeMap<LocalDateTime, Weather> restLDT = serviceAPI.getWeatherOfCityByYear(2024 ,c.getName());
            assertAll(
                    () -> assertNotNull(restLDT, "Liste sollte nicht null sein:")
            );
        }
    }
}