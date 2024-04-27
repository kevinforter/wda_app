package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.util.Util;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WeatherDAOImplTest {

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
            daoO.speichern(c);
            assertEquals(c, daoO.findById(c.getId()));
        }

        for (Weather w : Util.createWetterList()) {
            daoW.speichern(w);
            assertEquals(w, daoW.findById(w.getId()));
        }

    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_SaveWeather_ShouldGetAllWeatherDataInOneList(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c);
            assertEquals(c, daoO.findById(c.getId()));
        }

        List<Weather> listFromUtil = Util.createWetterList();

        for (Weather w : listFromUtil) {
            daoW.speichern(w);
            assertEquals(w, daoW.findById(w.getId()));
        }

        List<Weather> listFromDB = daoW.alle();
        assertEquals(listFromUtil.size(), listFromDB.size());
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_GetWeatherFromCityByDateTime_ShouldReturnOneWeather(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c);
            assertEquals(c, daoO.findById(c.getId()));
        }

        List<Weather> wetterList = Util.createWetterList();

        for (Weather w : wetterList) {
            daoW.speichern(w);
            assertEquals(w, daoW.findById(w.getId()));
        }
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_SaveAllWeather_ShouldSaveAllAsGetAllWeather(List<City> cityList) {

        WeatherDAO daoW = new WeatherDAOImpl();
        CityDAO daoO = new CityDAOImpl();

        for (City c : cityList) {
            daoO.speichern(c);
            assertEquals(c, daoO.findById(c.getId()));
        }

        daoW.saveAllWeather(Util.createWeatherMap());

        assertEquals(3, daoW.alle().size());
    }

    static Stream<List<City>> cityListProvider() {
        List<City> cities = Util.createCityList();
        return Stream.of(cities);
    }
}
