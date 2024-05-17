package ch.hslu.informatik.swde.wda.persister.util;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.impl.CityDAOImpl;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public class Util {

    private static final String puTEST = "testPU";

    private Util() {

    }

    public static void cleanDatabase() {

        EntityManager em = JpaUtil.createEntityManager(puTEST);
        em.getTransaction().begin();

        em.createQuery("DELETE FROM Weather e").executeUpdate();
        em.createQuery("DELETE FROM City e").executeUpdate();
        em.createQuery("DELETE FROM Init e").executeUpdate();

        em.getTransaction().commit();

        em.close();

    }

    public static List<City> createCityList() {

        List<City> list = new ArrayList<>();

        int[] zips = {3000, 8001, 1201, 7270, 8000, 6000, 1000, 2000};
        String[] cityNames = {"Bern", "Zurich", "Geneva", "Davos", "Basel", "Lucerne", "Lausanne", "Neuchatel"};
        String countryCode = "CH";

        for (int i = 0; i < zips.length; i++) {
            City city = new City(zips[i], cityNames[i], countryCode);
            list.add(city);
        }

        return list;
    }

    public static LinkedHashMap<Integer, City> createCityMap() {

        LinkedHashMap<Integer, City> cityMap = new LinkedHashMap<>();

        int[] zips = {3000, 8001, 1201, 7270, 8000, 6000, 1000, 2000};
        String[] cityNames = {"Bern", "Zurich", "Geneva", "Davos", "Basel", "Lucerne", "Lausanne", "Neuchatel"};
        String countryCode = "CH";

        for (int i = 0; i < zips.length; i++) {
            City city = new City(zips[i], cityNames[i], countryCode);
            cityMap.put(city.getZip(), city);
        }

        return cityMap;
    }

    public static List<Weather> createWetterList() {

        CityDAO daoO = new CityDAOImpl(puTEST);
        List<Weather> list = new ArrayList<>();

        List<City> cityList = createCityList();

        for (City city : cityList) {

            City ort = daoO.findCityByName(city.getName());

            Weather weather1 = new Weather(ort.getId(), LocalDateTime.of(2023, 12, 3, 22, 30, 19), "foggy", "fog", 23.0, 982.0, 91.0, 43.0, 10.0);
            Weather weather2 = new Weather(ort.getId(), LocalDateTime.of(2023, 12, 3, 23, 30, 19), "foggy", "fog", 23.0, 982.0, 91.0, 43.0, 10.0);
            Weather weather3 = new Weather(ort.getId(), LocalDateTime.now(), "foggy", "fog", 23.0, 982.0, 91.0, 43.0, 10.0);

            list.add(weather1);
            list.add(weather2);
            list.add(weather3);
        }

        return list;
    }

    public static TreeMap<LocalDateTime, Weather> createWeatherMap() {

        CityDAO daoO = new CityDAOImpl(puTEST);
        TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();

        List<City> cityList = createCityList();

        for (City city : cityList) {

            City ort = daoO.findCityByName(city.getName());

            Weather weather1 = new Weather(ort.getId(), LocalDateTime.of(2023, 12, 3, 21, 30, 19), "foggy", "fog", 23.0, 982.0, 91.0, 43.0, 10.0);
            Weather weather2 = new Weather(ort.getId(), LocalDateTime.of(2023, 12, 3, 22, 30, 19), "foggy", "fog", 23.0, 982.0, 91.0, 43.0, 10.0);
            Weather weather3 = new Weather(ort.getId(), LocalDateTime.of(2023, 12, 3, 23, 30, 19), "foggy", "fog", 23.0, 982.0, 91.0, 43.0, 10.0);

            weatherMap.put(weather1.getDTstamp(), weather1);
            weatherMap.put(weather2.getDTstamp(), weather2);
            weatherMap.put(weather3.getDTstamp(), weather3);
        }

        return weatherMap;
    }
}