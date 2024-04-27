package ch.hslu.informatik.swde.wda.business;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }


    void getWeatherOfCityByYear() {
    }
}