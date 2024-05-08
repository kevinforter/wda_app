package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public interface BusinessAPI {

    LinkedList<City> addAllCities();

    void addCurrentWeatherOfCity(String cityName);

    void addWeatherOfCityByYear(String city, int year);

    List<City> getAllCities();

    Weather getLatestWeatherOfCity(int cityId);

    Weather getLatestWeatherOfCity(String cityName);

    Weather getOldestWeatherOfCity(String cityName);

    List<LocalDateTime> getWeatherOfCityByYear(int cityId, int year);
}
