/**
 * Diese Schnittstelle gibt die Hauptfunktionalitäten vor, die für die
 * Persistierung von Objekten benötigt werden.
 *
 * @author Kevin Forter
 * @version 1.1.1
 */

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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.*;

public class BusinessImpl implements BusinessAPI {

    private static final Logger LOG = LoggerFactory.getLogger(BusinessImpl.class);

    private static final CityDAO daoC = new CityDAOImpl();
    private static final WeatherDAO daoW = new WeatherDAOImpl();
    private static final ApiReader reader = new ApiReaderImpl();

    /**
     * Adds all cities to the database.
     * <p>
     * This method reads city details from an external source using the ApiReader.
     * It then checks if the number of cities read is different from the number of cities currently in the database.
     * If the numbers are different, it means there are new cities to be added, so it saves all the cities read into the database.
     */
    @Override
    public void addAllCities() {
        try {
            // Read city details from an external source
            LinkedHashMap<Integer, City> cityRes = reader.readCities();
            Set<String> existingCities = daoC.allCityNames();

            // Check if the City already exist
            LinkedHashMap<Integer, City> citiesToSave = new LinkedHashMap<>();
            for (City c : cityRes.values()) {
                if (!existingCities.contains(c.getName())) citiesToSave.put(c.getZip(), c);
            }

            daoC.saveAllCities(citiesToSave);

        } catch (Exception e) {
            // Log the exception and handle it appropriately
            LOG.error("Error while adding cities: ", e);
        }
    }


    /**
     * Adds the current weather of a specified city to the database.
     * <p>
     * This method first finds the ID of the city by its name using the CityDAO.
     * If the city ID is not 0, it calls the private method addCurrentWeatherOfCity with the city ID and city name as parameters.
     * This private method retrieves the latest weather data of the city from both the database and an external API.
     *
     * @param cityName the name of the city for which the current weather data is to be added
     */
    @Override
    public void addCurrentWeatherOfCity(String cityName) {

        int cityId = daoC.findCityIdByName(cityName);
        if (cityId != 0) addCurrentWeatherOfCity(cityId, cityName);
    }

    /**
     * Adds the current weather of a specified city to the database.
     * <p>
     * This method first retrieves the latest weather data of the specified city from both the database and an external API.
     * If there is no existing weather data in the database for the city, it saves the current weather data from the API to the database.
     * If there is existing weather data in the database, it checks if the timestamp of the latest weather data from the database is different from the current weather data from the API.
     * If the timestamps are different, it calculates the time difference between the two timestamps.
     * If the time difference is less than 40 minutes, it saves the current weather data from the API to the database.
     * If the time difference is 40 minutes or more, it retrieves and saves the weather data of the city for the current year.
     *
     * @param cityId   the id of the city for which the current weather data is to be added
     * @param cityName the name of the city for which the current weather data is to be added
     */
    private static void addCurrentWeatherOfCity(int cityId, String cityName) {

        // Retrieve the latest weather data of the specified city from both the database and an external API
        Weather latestWeatherDAO = getLatestWeatherOfCity(cityId);
        Weather currentWeatherREADER = reader.readCurrentWeatherByCity(cityName);

        if (latestWeatherDAO == null) {

            // If there is no existing weather data in the database for the city, save the current weather data from the API to the database
            currentWeatherREADER.setCityId(cityId);
            daoW.speichern(currentWeatherREADER);

        } else if (currentWeatherREADER != null && !latestWeatherDAO.getDTstamp().isEqual(currentWeatherREADER.getDTstamp())) {

            // If there is existing weather data in the database, check if the timestamp of the latest weather data from the database is different from the current weather data from the API
            // Calculate the time difference between the two timestamps
            Duration diff = Duration.between(latestWeatherDAO.getDTstamp(), currentWeatherREADER.getDTstamp());

            if (diff.toMinutes() < 40) {

                // If the time difference is less than 40 minutes, save the current weather data from the API to the database
                currentWeatherREADER.setCityId(cityId);
                daoW.speichern(currentWeatherREADER);

            } else {

                // If the time difference is 40 minutes or more, retrieve and save the weather data of the city for the current year
                addWeatherOfCityByYear(cityId, reader.readWeatherByCityAndYear(cityName, Year.now().getValue()));

            }
        }
    }

    /**
     * Adds the weather data of a specified city for a specific year to the database.
     * <p>
     * This method first finds the ID of the city by its name using the CityDAO.
     * If the city ID is not 0, it calls the private method addWeatherOfCityByYear with the city ID, city name, and year as parameters.
     * This private method retrieves the weather data of the city for the specified year from an external API and the latest weather data of the city from the database.
     * If the size of the weather data retrieved from the API is different from the number of weather data in the database for the city, it means there are new weather data to be added.
     * So, it sets the city ID for each of the new weather data and saves them all to the database as a batch.
     *
     * @param cityName the name of the city for which the weather data is to be added
     * @param year     the year for which the weather data is to be added
     */
    @Override
    public void addWeatherOfCityByYear(String cityName, int year) {

        int cityId = daoC.findCityIdByName(cityName);
        if (cityId != 0) addWeatherOfCityByYear(cityId, reader.readWeatherByCityAndYear(cityName, year));
    }

    /**
     * Adds the weather data of a specified city for a specific year to the database.
     * <p>
     * This method first checks if there are any existing weather data in the database for the specified city.
     * If there is no existing weather data, it adds the current weather data of the city to the database.
     * It then retrieves the weather data of the city for the specified year from an external API and the latest weather data of the city from the database.
     * If the size of the weather data retrieved from the API is different from the number of weather data in the database for the city, it means there are new weather data to be added.
     * So, it sets the city ID for each of the new weather data and saves them all to the database as a batch.
     *
     * @param cityId the id of the city for which the weather data is to be added
     */
    private static void addWeatherOfCityByYear(int cityId, TreeMap<LocalDateTime, Weather> weatherMap) {

        // If the size of the weather data retrieved from the API is different from the number of weather data in the database for the city
        if (weatherMap.size() != daoW.getNumberOfWeatherByCity(cityId)) {

            // Set the city ID for each of the new weather data
            for (Weather weather : weatherMap.values()) {
                weather.setCityId(cityId);
            }

            // Save all the new weather data to the database as a batch
            daoW.saveAllWeather(weatherMap, cityId);
        }
    }

    /**
     * Retrieves a city object by its name.
     * <p>
     * This method uses the CityDAO to find a city by its name in the database.
     * It returns the city object if found, otherwise it returns null.
     *
     * @param name the name of the city to be retrieved
     * @return the city object if found, otherwise null
     */
    @Override
    public City getCityByName(String name) {
        return daoC.findCityByName(name);
    }

    /**
     * Retrieves a list of all cities in the database.
     * <p>
     * This method uses the CityDAO to retrieve all cities from the database.
     * It returns a list of city objects.
     *
     * @return a list of all city objects in the database
     */
    @Override
    public List<City> getAllCities() {
        return daoC.alle();
    }

    /**
     * Retrieves the current weather of a specified city.
     * <p>
     * This method first finds the ID of the city by its name using the CityDAO.
     * If the city ID is not 0, it retrieves the current weather of the city by its ID and name.
     * If the city ID is 0, it returns a new weather object.
     *
     * @param cityName the name of the city for which the current weather is to be retrieved
     * @return the current weather of the city if the city ID is not 0, otherwise a new weather object
     */
    @Override
    public Weather getCurrentWeatherOfCity(String cityName) {
        int cityId = daoC.findCityIdByName(cityName);
        return cityId != 0 ? getCurrentWeatherOfCity(cityId, cityName) : new Weather();
    }

    /**
     * Retrieves the current weather of a specified city by its ID and name.
     * <p>
     * This method first retrieves the latest weather data of the city by its ID from the database and the current weather data of the city by its name from an external API.
     * If the timestamps of the two weather data are the same, it returns the weather data from the database.
     * If the timestamps are different, it adds the current weather data of the city to the database and retrieves it again.
     *
     * @param cityId   the ID of the city for which the current weather is to be retrieved
     * @param cityName the name of the city for which the current weather is to be retrieved
     * @return the current weather of the city
     */
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

    /**
     * Retrieves the latest weather of a specified city by its name.
     * <p>
     * This method first finds the ID of the city by its name using the CityDAO.
     * If the city ID is not 0, it retrieves the latest weather of the city by its ID.
     * If the city ID is 0, it returns a new weather object.
     *
     * @param cityName the name of the city for which the latest weather is to be retrieved
     * @return the latest weather of the city if the city ID is not 0, otherwise a new weather object
     */
    @Override
    public Weather getLatestWeatherOfCity(String cityName) {
        int cityId = daoC.findCityIdByName(cityName);
        return cityId != 0 ? getLatestWeatherOfCity(cityId) : new Weather();
    }

    /**
     * Retrieves the latest weather of a specified city by its ID.
     * <p>
     * This method uses the WeatherDAO to find the latest weather of a city by its ID in the database.
     * It returns the weather object if found.
     *
     * @param cityId the ID of the city for which the latest weather is to be retrieved
     * @return the latest weather of the city
     */
    private static Weather getLatestWeatherOfCity(int cityId) {
        return daoW.findLatestWeatherByCity(cityId);
    }

    /**
     * Retrieves the weather of a specified city for a specific year by its name.
     * <p>
     * This method first finds the ID of the city by its name using the CityDAO.
     * If the city ID is not 0, it retrieves the weather of the city for the specified year by its ID.
     * If the city ID is 0, it returns an empty TreeMap.
     *
     * @param year     the year for which the weather is to be retrieved
     * @param cityName the name of the city for which the weather is to be retrieved
     * @return a TreeMap of the weather of the city for the specified year if the city ID is not 0, otherwise an empty TreeMap
     */
    @Override
    public TreeMap<LocalDateTime, Weather> getWeatherOfCityByYear(int year, String cityName) {
        int cityId = daoC.findCityIdByName(cityName);
        return cityId != 0 ? getWeatherOfCityByYear(year, cityId) : new TreeMap<>();
    }

    /**
     * Retrieves the weather of a specified city for a specific year by its ID.
     * <p>
     * This method uses the WeatherDAO to find the weather of a city for a specific year by its ID in the database.
     * It returns a TreeMap of the weather if found.
     *
     * @param year   the year for which the weather is to be retrieved
     * @param cityId the ID of the city for which the weather is to be retrieved
     * @return a TreeMap of the weather of the city for the specified year
     */
    private static TreeMap<LocalDateTime, Weather> getWeatherOfCityByYear(int year, int cityId) {
        return daoW.findWeatherFromCityByYear(year, cityId);
    }

    /**
     * Retrieves the weather of a specified year
     * <p>
     * This method first finds the ID of the city by its name using the CityDAO.
     * If the city ID is not 0, it retrieves the weather of the city for the specified year by its ID.
     * If the city ID is 0, it returns an empty TreeMap.
     *
     * @param year the year for which the weather is to be retrieved
     * @return a TreeMap of the weather of the city for the specified year if the city ID is not 0, otherwise an empty TreeMap
     */
    @Override
    public TreeMap<LocalDateTime, Weather> getWeatherByYear(int year) {
        return isValidYear(year) ? daoW.findWeatherByYear(year) : new TreeMap<>();
    }

    private static boolean isValidYear(int year) {
        int currentYear = new GregorianCalendar().get(Calendar.YEAR);
        return year >= 1 && year <= currentYear;
    }
}
