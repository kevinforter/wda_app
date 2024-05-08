package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.impl.CityDAOImpl;
import ch.hslu.informatik.swde.wda.persister.impl.WeatherDAOImpl;
import ch.hslu.informatik.swde.wda.reader.ApiReader;
import ch.hslu.informatik.swde.wda.reader.ApiReaderImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

public class BusinessImpl implements BusinessAPI {

    private static final CityDAO daoC = new CityDAOImpl();
    private static final WeatherDAO daoW = new WeatherDAOImpl();
    private static final ApiReader reader = new ApiReaderImpl();

    @Override
    public void addAllCities() {

        LinkedHashMap<Integer, City> cityRes = reader.readCityDetailsList(reader.readCityNames());

        if (cityRes.size() != daoC.getNumberOfCities()) {

            // cityRes als Batch speichern
            daoC.saveAllCities(cityRes);
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

        // Überprüfe ob Wetterdaten vorhanden sind
        if (!daoW.ifWeatherOfCityExist(cityName)) {
            addCurrentWeatherOfCity(cityName);
        }

        // API call der Wetterdaten
        TreeMap<LocalDateTime, Weather> weatherMap = reader.readWeatherByCityAndYear(cityName, year);
        Weather latestWeather = getLatestWeatherOfCity(cityName);

        if (weatherMap.size() != daoW.getNumberOfWeatherByCity(cityName)) {

            // CityId setzen
            for (Weather weather : weatherMap.values()) {
                weather.setCityId(latestWeather.getCityId());
            }

            // Als Batch speicher
            daoW.saveAllWeather(weatherMap, cityName);
        }
    }

    @Override
    public City getCityByName(String name) {

        return daoC.findCityByName(name);
    }

    @Override
    public List<City> getAllCities() {

        return daoC.alle();
    }

    @Override
    public Weather getCurrentWeatherOfCity(String cityName) {

        int cityId = daoC.findCityIdByName(cityName);
        return cityId != 0 ? getCurrentWeatherOfCity(cityId, cityName) : new Weather();
    }

    private Weather getCurrentWeatherOfCity(int cityId, String cityName) {

        Weather daoWeather = daoW.findLatestWeatherByCity(cityId);
        Weather readerWeather = reader.readCurrentWeatherByCity(cityName);

        if (daoWeather.getDTstamp().isEqual(readerWeather.getDTstamp())) {
            return daoWeather;
        } else {
            addCurrentWeatherOfCity(cityName);
            return getCurrentWeatherOfCity(cityName);
        }
    }

    @Override
    public Weather getLatestWeatherOfCity(String cityName) {

        int cityId = daoC.findCityIdByName(cityName);
        return cityId != 0 ? getLatestWeatherOfCity(cityId) : new Weather();
    }

    private static Weather getLatestWeatherOfCity(int cityId) {

        return daoW.findLatestWeatherByCity(cityId);

    }

    @Override
    public TreeMap<LocalDateTime, Weather> getWeatherOfCityByYear(int year, String cityName) {

        int cityId = daoC.findCityIdByName(cityName);
        return cityId != 0 ? getWeatherOfCityByYear(year, cityId) : new TreeMap<>();
    }

    private static TreeMap<LocalDateTime, Weather> getWeatherOfCityByYear(int year, int cityId) {

        return daoW.findWeatherFromCityByYear(year, cityId);
    }
}
