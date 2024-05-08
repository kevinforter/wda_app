package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

public interface BusinessAPI {

    void addAllCities();

    void addCurrentWeatherOfCity(String cityName);

    void addWeatherOfCityByYear(String city, int year);

    City getCityByName(String name);

    List<City> getAllCities();

    Weather getLatestWeatherOfCity(String cityName);

    TreeMap<LocalDateTime, Weather> getWeatherOfCityByYear(int year, String cityName);
}
