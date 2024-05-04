package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

public interface BusinessAPI {

    void addAllCities();

    void addCurrentWeatherOfCity(String cityName);

    void addWeatherOfCityByYear(String city, int year);

    List<City> getAllCities();

    Weather getCurrentWeatherOfCity(int cityId);

    Weather getCurrentWeatherOfCity(String cityName);

    Weather getOldestWeatherOfCity(String cityName);

    List<LocalDateTime> getWeatherOfCityByYear(int cityId, int year);
}
