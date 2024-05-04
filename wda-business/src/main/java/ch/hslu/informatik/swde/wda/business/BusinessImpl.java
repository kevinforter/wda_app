package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.impl.CityDAOImpl;
import ch.hslu.informatik.swde.wda.persister.impl.WeatherDAOImpl;
import ch.hslu.informatik.swde.wda.reader.ApiReader;
import ch.hslu.informatik.swde.wda.reader.ApiReaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

public class BusinessImpl implements BusinessAPI {

    private static final CityDAO daoC = new CityDAOImpl();
    private static final WeatherDAO daoW = new WeatherDAOImpl();
    private static final ApiReader reader = new ApiReaderImpl();

    private static final Logger LOG = LoggerFactory.getLogger(BusinessImpl.class);

    @Override
    public void addAllCities() {

        LinkedHashMap<Integer, City> cityRes = reader.readCityDetailsList(reader.readCityNames());
        List<String> cityList = daoC.allCityNames();

        // Iteration über die HashMap cityRes
        for (City city : cityRes.values()) {

            if (!cityList.contains(city.getName())) {
                // Falls die Stadt nicht in der DB ist, wird sie gespeichert
                daoC.speichern(city);
            }
        }
    }

    @Override
    public void addCurrentWeatherOfCity(String cityName) {

        // Wetterdaten von DB und API abfragen
        Weather currentWeatherDAO = getCurrentWeatherOfCity(cityName);
        Weather currentWeatherREADER = reader.readCurrentWeatherByCity(cityName);

        if (currentWeatherDAO == null) {

            // Aktuelles Wetter von API holen und speichern
            currentWeatherREADER.setCityId(daoC.findCityIdByName(cityName));
            daoW.speichern(currentWeatherREADER);

        } else if (currentWeatherREADER != null && !currentWeatherDAO.getDTstamp().isEqual(currentWeatherREADER.getDTstamp())) {

            // Zeitunterschied zwischen DB und API Wetter
            Duration diff = Duration.between(currentWeatherDAO.getDTstamp(), currentWeatherREADER.getDTstamp());

            if (diff.toMinutes() < 40) {

                // Aktuelles Wetter von API holen und speichern
                currentWeatherREADER.setCityId(currentWeatherDAO.getCityId());
                daoW.speichern(currentWeatherREADER);

            } else {

                // Wetter vom ganzen Jahr holen
                addWeatherOfCityByYear(cityName, Year.now().getValue());

            }
        }
    }


    @Override
    public void addWeatherOfCityByYear(String cityName, int year) {

        LinkedHashMap<LocalDateTime, Weather> weatherMap = reader.readWeatherByCityAndYear(cityName, year);
        Weather currentWeather = getCurrentWeatherOfCity(cityName);
        List<LocalDateTime> weatherList = getWeatherOfCityByYear(year, currentWeather.getCityId());

        for (Weather weather : weatherMap.values()) {
            if (!weatherList.contains(weather.getDTstamp())) {
                weather.setCityId(currentWeather.getCityId());
                daoW.speichern(weather);
            }
        }
    }

    @Override
    public List<City> getAllCities() {

        return daoC.alle();
    }

    @Override
    public Weather getCurrentWeatherOfCity(int cityId) {

        return daoW.findLatestWeatherByCity(cityId);

    }

    @Override
    public Weather getCurrentWeatherOfCity(String cityName) {

        return daoW.findLatestWeatherByCity(daoC.findCityByName(cityName).getId());
    }

    @Override
    public List<LocalDateTime> getWeatherOfCityByYear(int year, int cityId) {

        return daoW.findWeatherFromCityByYear(year, cityId);
    }
}
