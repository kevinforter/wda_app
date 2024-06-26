package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.business.util.Util;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class BusinessImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessImplTest.class);

    private static final String  puTEST = "testPU";

    @BeforeEach
    void setUp() {
        Util.cleanDatabase();
    }

    @AfterEach
    void clearUp() {
        Util.cleanDatabase();
    }

    @AfterAll
    static void tearDown() {
        Util.cleanDatabase();
    }


    @Tag("integration")
    @Test
    void addAllCities() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        long start = System.currentTimeMillis();
        serviceAPI.addAllCities();
        long end = System.currentTimeMillis();

        long time = end - start;
        System.out.println(time);
        LOG.info("Time used to save all Cities: " + time + " ms");
    }

    @Tag("integration")
    @Test
    void addCurrentWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        serviceAPI.addAllCities();
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
    void addCurrentWeatherOfCity_butLatestIsOneDayOlder() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        Util.saveDummyWeather();
        List<City> resCityList = serviceAPI.getAllCities();
        assertEquals(1, resCityList.size());

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

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);
        serviceAPI.addAllCities();

        long start = System.currentTimeMillis();
        serviceAPI.addWeatherOfCityByYear("Davos", 2024);
        long end = System.currentTimeMillis();

        long time = end - start;
        LOG.info("Time used to get and save all weather Data: " + time + " ms");

    }

    @Tag("integration")
    @Test
    void addWeatherOfAllCitiesByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        serviceAPI.addAllCities();
        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {

            long start = System.currentTimeMillis();
            serviceAPI.addWeatherOfCityByYear(c.getName(), 2024);
            long end = System.currentTimeMillis();

            long time = end - start;
            LOG.info("Time used to get and save all weather Data: " + time + " ms");
        }
    }

    @Tag("integration")
    @Test
    void getAllCities() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {
            LOG.info("City : " + c);
        }
    }

    @Tag("integration")
    @Test
    void getLatestWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        List<City> resCity = serviceAPI.getAllCities();
        for (City c : resCity) {
            Weather currentWeather = serviceAPI.getLatestWeatherOfCity(c.getName());
            LOG.info("Current Weather: " + currentWeather);
        }
    }

    @Tag("integration")
    @Test
    void getWeatherOfCityByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        serviceAPI.addAllCities();

        List<City> cityList = serviceAPI.getAllCities();

        for (City c : cityList) {
            serviceAPI.addCurrentWeatherOfCity(c.getName());
            TreeMap<LocalDateTime, Weather> restLDT = serviceAPI.getWeatherOfCityByYear(2024, c.getName());
            assertAll(
                    () -> assertNotNull(restLDT, "Liste sollte nicht null sein:")
            );
        }
    }

    @Tag("integration")
    @Test
    void getWeatherOfCityByTimespan() {

        final BusinessAPI serviceAPI = new BusinessImpl(puTEST);

        serviceAPI.addAllCities();
        serviceAPI.addWeatherOfCityByYear("Davos", Year.now().getValue());
        TreeMap<LocalDateTime, Weather> resWeather = serviceAPI.getWeatherByCityAndTimeSpan("Davos" ,LocalDateTime.now().minusMonths(1), LocalDateTime.now());

        assertAll(
                () -> assertNotNull(resWeather, "Liste sollte nicht null sein:"),
                () -> assertEquals(resWeather.lastEntry().getValue().getDTstamp(), serviceAPI.getCurrentWeatherOfCity("Davos").getDTstamp(), "Datum sollte das gleiche sein")
        );

    }
}