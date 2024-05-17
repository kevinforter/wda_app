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

    private static final String puTEST = "testPU";

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

        final BusinessAPI serviceAPI = new BusinessImpl();

        long start = System.currentTimeMillis();
        serviceAPI.addAllCities(puTEST);
        long end = System.currentTimeMillis();

        long time = end - start;
        System.out.println(time);
        LOG.info("Time used to save all Cities: " + time + " ms");
    }

    @Tag("integration")
    @Test
    void addCurrentWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        serviceAPI.addAllCities(puTEST);
        List<City> resCityList = serviceAPI.getAllCities(puTEST);

        for (City c : resCityList) {

            long start = System.currentTimeMillis();
            serviceAPI.addCurrentWeatherOfCity(c.getName(), puTEST);
            long end = System.currentTimeMillis();

            long time = end - start;
            LOG.info("Time used to save current Weather: " + time + " ms");
        }
    }

    @Tag("integration")
    @Test
    void addCurrentWeatherOfCity_butLatestIsOneDayOlder() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        Util.saveDummyWeather();
        List<City> resCityList = serviceAPI.getAllCities(puTEST);
        assertEquals(1, resCityList.size());

        for (City c : resCityList) {

            long start = System.currentTimeMillis();
            serviceAPI.addCurrentWeatherOfCity(c.getName(), puTEST);
            long end = System.currentTimeMillis();

            long time = end - start;
            LOG.info("Time used to save current Weather: " + time + " ms");
        }
    }

    @Tag("integration")
    @Test
    void addWeatherOfCityByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl();
        serviceAPI.addAllCities(puTEST);

        long start = System.currentTimeMillis();
        serviceAPI.addWeatherOfCityByYear("Davos", 2024, puTEST);
        long end = System.currentTimeMillis();

        long time = end - start;
        LOG.info("Time used to get and save all weather Data: " + time + " ms");

    }

    @Tag("integration")
    @Test
    void addWeatherOfAllCitiesByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        serviceAPI.addAllCities(puTEST);
        List<City> resCity = serviceAPI.getAllCities(puTEST);
        for (City c : resCity) {

            long start = System.currentTimeMillis();
            serviceAPI.addWeatherOfCityByYear(c.getName(), 2024, puTEST);
            long end = System.currentTimeMillis();

            long time = end - start;
            LOG.info("Time used to get and save all weather Data: " + time + " ms");
        }
    }

    @Tag("integration")
    @Test
    void getAllCities() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCity = serviceAPI.getAllCities(puTEST);
        for (City c : resCity) {
            LOG.info("City : " + c);
        }
    }

    @Tag("integration")
    @Test
    void getLatestWeatherOfCity() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        List<City> resCity = serviceAPI.getAllCities(puTEST);
        for (City c : resCity) {
            Weather currentWeather = serviceAPI.getLatestWeatherOfCity(c.getName(), puTEST);
            LOG.info("Current Weather: " + currentWeather);
        }
    }

    @Tag("integration")
    @Test
    void getWeatherOfCityByYear() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        serviceAPI.addAllCities(puTEST);

        List<City> cityList = serviceAPI.getAllCities(puTEST);

        for (City c : cityList) {
            serviceAPI.addCurrentWeatherOfCity(c.getName(), puTEST);
            TreeMap<LocalDateTime, Weather> restLDT = serviceAPI.getWeatherOfCityByYear(2024, c.getName(), puTEST);
            assertAll(
                    () -> assertNotNull(restLDT, "Liste sollte nicht null sein:")
            );
        }
    }

    @Tag("integration")
    @Test
    void getWeatherOfCityByTimespan() {

        final BusinessAPI serviceAPI = new BusinessImpl();

        serviceAPI.addAllCities(puTEST);
        serviceAPI.addWeatherOfCityByYear("Davos", Year.now().getValue(), puTEST);
        TreeMap<LocalDateTime, Weather> resWeather = serviceAPI.getWeatherByCityAndTimeSpan("Davos" ,LocalDateTime.now().minusMonths(1), LocalDateTime.now(), puTEST);

        assertAll(
                () -> assertNotNull(resWeather, "Liste sollte nicht null sein:"),
                () -> assertEquals(resWeather.lastEntry().getValue().getDTstamp(), serviceAPI.getCurrentWeatherOfCity("Davos", puTEST).getDTstamp(), "Datum sollte das gleiche sein")
        );

    }
}