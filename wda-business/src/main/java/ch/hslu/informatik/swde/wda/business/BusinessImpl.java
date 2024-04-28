package ch.hslu.informatik.swde.wda.business;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.persister.DAO.CityDAO;
import ch.hslu.informatik.swde.wda.persister.DAO.WeatherDAO;
import ch.hslu.informatik.swde.wda.persister.impl.CityDAOImpl;
import ch.hslu.informatik.swde.wda.persister.impl.WeatherDAOImpl;
import ch.hslu.informatik.swde.wda.reader.ApiReader;
import ch.hslu.informatik.swde.wda.reader.ApiReaderImpl;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class BusinessImpl implements BusinessAPI{

    private static final CityDAO daoC = new CityDAOImpl();
    private static final WeatherDAO daoW = new WeatherDAOImpl();
    private static final ApiReader reader = new ApiReaderImpl();
    @Override
    public void addAllCities() {

        if(daoC.alle().isEmpty()) {
            LinkedHashMap<Integer, City> cityRes = reader.readCityDetailsList(reader.readCityNames());

            daoC.saveAllCities(cityRes);
        }
    }

    @Override
    public void addCurrentWeatherOfCity() {

    }

    @Override
    public void addWeatherOfCityByYear() {

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
    public LinkedHashMap<LocalDateTime, Weather> getWeatherOfCityByYear() {
        return null;
    }
}
