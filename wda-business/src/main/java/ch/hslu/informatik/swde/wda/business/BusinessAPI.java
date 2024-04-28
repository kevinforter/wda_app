package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public interface BusinessAPI {

    void addAllCities();

    void addCurrentWeatherOfCity();

    void addWeatherOfCityByYear();

    LinkedHashMap<Integer, City> getAllCities();

    Weather getCurrentWeatherOfCity(int cityId);

    LinkedHashMap<LocalDateTime, Weather> getWeatherOfCityByYear();
}
