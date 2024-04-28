package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

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


    void addCurrentWeatherOfCity() {
    }


    void addWeatherOfCityByYear() {
    }


    void getAllCities() {
    }


    void getCurrentWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        LinkedHashMap<Integer, City> resCity = serviceAPI.getAllCities();
        for(City c : resCity.values()) {
            Weather currentWeather = serviceAPI.getCurrentWeatherOfCity(c.getId());
            LOG.info("Current Weather: " + currentWeather);
        }
    }


    void getWeatherOfCityByYear() {
    }
}