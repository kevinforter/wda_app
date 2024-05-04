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
        Weather latestWeatherDAO = getLatestWeatherOfCity(cityName);
        Weather currentWeatherREADER = reader.readCurrentWeatherByCity(cityName);

        if (latestWeatherDAO == null) {

            // Aktuelles Wetter von API holen und speichern
            currentWeatherREADER.setCityId(daoC.findCityIdByName(cityName));
            daoW.speichern(currentWeatherREADER);

        } else if (currentWeatherREADER != null && !latestWeatherDAO.getDTstamp().isEqual(currentWeatherREADER.getDTstamp())) {

            // Zeitunterschied zwischen DB und API Wetter
            Duration diff = Duration.between(latestWeatherDAO.getDTstamp(), currentWeatherREADER.getDTstamp());

            if (diff.toMinutes() < 40) {

                // Aktuelles Wetter von API holen und speichern
                currentWeatherREADER.setCityId(latestWeatherDAO.getCityId());
                daoW.speichern(currentWeatherREADER);

            } else {

                // Wetter vom ganzen Jahr holen
                addWeatherOfCityByYear(cityName, Year.now().getValue());

            }
        }
    }


    @Override
    public void addWeatherOfCityByYear(String cityName, int year) {

        if (!daoW.ifWeatherOfCityExist(cityName)) {
            addCurrentWeatherOfCity(cityName);
        } else {
            LinkedHashMap<LocalDateTime, Weather> weatherMap = reader.readWeatherByCityAndYear(cityName, year);
            Weather latestWeatherObj = getLatestWeatherOfCity(cityName);

            LocalDateTime latestWeather = latestWeatherObj.getDTstamp();
            LocalDateTime oldestWeather = getOldestWeatherOfCity(cityName).getDTstamp();

            LinkedHashMap<LocalDateTime, Weather> weatherToPersist = new LinkedHashMap<>();

            for (Weather weather : weatherMap.values()) {
                if (weather.getDTstamp().isBefore(oldestWeather) || weather.getDTstamp().isAfter(latestWeather)) {
                    weather.setCityId(latestWeatherObj.getCityId());
                    weatherToPersist.put(weather.getDTstamp(), weather);
                }
            }
            daoW.saveAllWeather(weatherToPersist);
        }
    }

    @Override
    public List<City> getAllCities() {

        return daoC.alle();
    }

    @Override
    public Weather getLatestWeatherOfCity(int cityId) {

        return daoW.findLatestWeatherByCity(cityId);

    }

    @Override
    public Weather getLatestWeatherOfCity(String cityName) {

        return daoW.findLatestWeatherByCity(daoC.findCityIdByName(cityName));
    }

    @Override
    public Weather getOldestWeatherOfCity(String cityName) {

        return daoW.findOldestWeatherByCity(daoC.findCityIdByName(cityName));
    }

    @Override
    public List<LocalDateTime> getWeatherOfCityByYear(int year, int cityId) {

        return daoW.findWeatherFromCityByYear(year, cityId);
    }
}
