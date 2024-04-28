package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BusinessImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessImplTest.class);

    @Test
    void addAllCities() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        long start = System.currentTimeMillis();
        serviceAPI.addAllCities();
        long end = System.currentTimeMillis();

        long time = end - start;
        LOG.info("Time used to save all Cities: " + time + " ms");
    }


    @Test
    void addCurrentWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCityList = serviceAPI.getAllCities();

        for (City c : resCityList) {

            long start = System.currentTimeMillis();
            serviceAPI.addCurrentWeatherOfCity(c.getId());
            long end = System.currentTimeMillis();

            long time = end - start;
            LOG.info("Time used to save current Weather: " + time + " ms");
        }
    }


    void addWeatherOfCityByYear() {
    }

    @Test
    void getAllCities() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {
            LOG.info("City : " + c);
        }
    }

    @Test
    void getCurrentWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {
            Weather currentWeather = serviceAPI.getCurrentWeatherOfCity(c.getId());
            LOG.info("Current Weather: " + currentWeather);
        }
    }


    void getWeatherOfCityByYear() {
    }
}