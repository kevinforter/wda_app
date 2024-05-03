package ch.hslu.informatik.swde.wda.persister.impl;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.util.Util;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class CityDAOImplTest {

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
    void test_SavingCity_ShouldBeSameAsFoundByID(List<City> listFromUtil) {

        CityDAO dao = new CityDAOImpl();

        for (City c : listFromUtil) {
            dao.speichern(c);
            assertEquals(c, dao.findById(c.getId()), "Objekte stimmen nicht überein");
        }

    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_SavingCity_ShouldBeSameAsFoundByName(List<City> listFromUtil) {

        CityDAO dao = new CityDAOImpl();

        for (City c : listFromUtil) {
            dao.speichern(c);
            assertEquals(c, dao.findCityByName(c.getName()), "Objekte stimmen nicht überein");
        }
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_SavingCity_ShouldBeSameAsFoundByFieldNameAndValue(List<City> listFromUtil) {

        CityDAO dao = new CityDAOImpl();

        for (City c : listFromUtil) {
            dao.speichern(c);
            assertEquals(c, dao.findEntityByFieldAndString("name", c.getName()), "Objekte stimmen nicht überein");
        }
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityListProvider")
    void test_getAllCitiesFormDB_ShouldBeTheSameAsSavedList(List<City> listFromUtil) {

        CityDAO dao = new CityDAOImpl();
        List<City> listFromDB;

        for (City c : listFromUtil) {
            dao.speichern(c);
            assertEquals(c, dao.findById(c.getId()));
        }

        listFromDB = dao.alle();
        assertEquals(listFromUtil, listFromDB, "DB ist nicht kongruent zur Liste");
    }

    @Tag("unittest")
    @ParameterizedTest
    @MethodSource("cityMapProvider")
    void test_saveAllCities_ShouldMakeABulkSave(LinkedHashMap<Integer, City> mapFromUtil) {

        CityDAO dao = new CityDAOImpl();

        dao.saveAllCities(mapFromUtil);
        assertEquals(mapFromUtil.size(), dao.alle().size(), "Array not the Same");

    }

    @Tag("unittest")
    @Test
    void checkIfTableExist_ShouldReturnBoolean() {

        CityDAO dao = new CityDAOImpl();

        boolean status = dao.ifTableExist();
        assumeTrue(status, "Table was empty");

    }


    static Stream<List<City>> cityListProvider() {
        List<City> cities = Util.createCityList();
        return Stream.of(cities);
    }

    static Stream<LinkedHashMap<Integer, City>> cityMapProvider() {
        LinkedHashMap<Integer, City> cities = Util.createCityMap();
        return Stream.of(cities);
    }
}