package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.util.Util;
import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class WeatherDAOImplTest {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherDAOImplTest.class);
    
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

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_SaveWeather_ShouldGetCorrectWeatherByID(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);
            assertEquals(c, daoO.findById(c.getId(), puTEST));
        }

        for (Weather w : Util.createWetterList()) {
            daoW.speichern(w, puTEST);
            assertEquals(w, daoW.findById(w.getId(), puTEST));
        }

    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_SaveWeather_ShouldGetAllWeatherDataInOneList(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);
            assertEquals(c, daoO.findById(c.getId(), puTEST));
        }

        List<Weather> listFromUtil = Util.createWetterList();

        for (Weather w : listFromUtil) {
            daoW.speichern(w, puTEST);
            assertEquals(w, daoW.findById(w.getId(), puTEST));
        }

        List<Weather> listFromDB = daoW.alle(puTEST);
        assertEquals(listFromUtil.size(), listFromDB.size());
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_GetWeatherFromCityByDateTime_ShouldReturnOneWeather(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);
            assertEquals(c, daoO.findById(c.getId(), puTEST));
        }

        List<Weather> wetterList = Util.createWetterList();

        for (Weather w : wetterList) {
            daoW.speichern(w, puTEST);
            assertEquals(w, daoW.findById(w.getId(), puTEST));
        }
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_GetWeatherFromCityByYear_ShouldReturnMultibleWeather(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);
            assertEquals(c, daoO.findById(c.getId(), puTEST));
        }

        List<Weather> wetterList = Util.createWetterList();

        for (Weather w : wetterList) {
            daoW.speichern(w, puTEST);
            assertEquals(w, daoW.findById(w.getId(), puTEST));
        }

        List<LocalDateTime> weatherRes = daoW.findWeatherDateFromCityByYear(2024, daoO.findCityByName("Davos", puTEST).getId(), puTEST);
        List<LocalDateTime> weatherResNull = daoW.findWeatherDateFromCityByYear(2025, daoO.findCityByName("Davos", puTEST).getId(), puTEST);

        assertAll(
                () -> assertNotNull(weatherRes, "2024 sollte nicht eine leere Liste ergeben"),
                () -> assertTrue(weatherResNull.isEmpty(), "2025 sollte leer sein")
        );
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_GetWeatherFromCityByMonth_ShouldReturnMultibleWeather(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);
            assertEquals(c, daoO.findById(c.getId(), puTEST));
        }

        List<Weather> wetterList = Util.createWetterList();

        for (Weather w : wetterList) {
            daoW.speichern(w, puTEST);
            assertEquals(w, daoW.findById(w.getId(), puTEST));
        }

        TreeMap<LocalDateTime, Weather> weatherRes = daoW.findWeatherFromCityByMonth(3, daoO.findCityByName("Davos", puTEST).getId(), puTEST);
        TreeMap<LocalDateTime, Weather> weatherResNull = daoW.findWeatherFromCityByMonth(12, daoO.findCityByName("Davos", puTEST).getId(), puTEST);

        assertAll(
                () -> assertNotNull(weatherRes, "2024 sollte nicht eine leere Liste ergeben"),
                () -> assertTrue(weatherResNull.isEmpty(), "2025 sollte leer sein")
        );
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void findWeatherFromCityByDateTime_ShouldReturnNull_WhenWeatherDoesNotExist(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);
            assertEquals(c, daoO.findById(c.getId(), puTEST));
        }

        List<Weather> wetterList = Util.createWetterList();

        for (Weather w : wetterList) {
            daoW.speichern(w, puTEST);
            assertEquals(w, daoW.findById(w.getId(), puTEST));
        }

        Weather resWeather = daoW.findWeatherFromCityByDateTime(LocalDateTime.now(), daoO.findCityIdByName("Davos", puTEST), puTEST);

        assertNotNull(resWeather, "It should not be null");

    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_SaveAllWeather_ShouldSaveAllAsGetAllWeather(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);
            assertEquals(c, daoO.findById(c.getId(), puTEST));
        }

        daoW.saveAllWeather(Util.createWeatherMap(), daoO.findCityIdByName("Neuchatel", puTEST), puTEST);

        assertEquals(3, daoW.alle(puTEST).size());
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void checkIfTableExist_ShouldReturnBoolean(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c, puTEST);

            assertAll(
                    () -> assertEquals(c, daoO.findById(c.getId(), puTEST), "City Wasn't in List"),
                    () -> assertFalse(daoW.ifWeatherOfCityExist(c.getId(), puTEST), "Table was empty")
            );
        }
    }

    static Stream<List<City>> cityListProvider() {
        List<City> cities = Util.createCityList();
        return Stream.of(cities);
    }
}
