/**
 * Diese Schnittstelle gibt die Implementierung vor, welche für die Abfrage
 * von Wetterdaten und Ortschaften benötigt werden.
 *
 * @author Kevin
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.reader;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Diese Klasse stellt eine konkrete Implementierung der Schnittstelle 'ApiReader' dar.
 */

public class ApiReaderImpl implements ApiReader {

    private static final Logger LOG = LoggerFactory.getLogger(ApiReaderImpl.class);
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URI = "http://eee-03318.simple.eee.intern:8080/";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String format = "application/json";

    /*-----------------------------------------------CITY API REQUEST-----------------------------------------------*/

    /**
     * Retrieves a list of city names from the weather data provider.
     * <p>
     * This method sends a GET request to the weather data provider's API endpoint for cities.
     * If the response status code is 200, it reads the JSON response body and extracts the city names.
     * The city names are then URL-encoded and added to a LinkedList.
     * If the response status code is not 200, it logs an error message and returns an empty LinkedList.
     * If the LinkedList of city names is empty after processing the JSON response,
     * it logs a message and returns the empty LinkedList.
     * If an exception occurs during the execution of the method,
     * it logs an error message and throws a RuntimeException.
     *
     * @return a LinkedList of URL-encoded city names, or an empty LinkedList if no city names are found or an error occurs
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public LinkedList<String> readCityNames() {
        try {
            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata/cities/");
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            LinkedList<String> cityNames = new LinkedList<>();
            if (res.statusCode() == 200) {

                JsonNode node = mapper.readTree(res.body());
                for (JsonNode n : node) {

                    String name = n.get("name").asText();
                    String encodedCityName = name.replace(" ", "+");

                    cityNames.add(encodedCityName);
                }

            } else {
                // Log-Eintrag machen
                LOG.info("Error occurred, Status code: " + res.statusCode());
                return new LinkedList<>();
            }

            if (cityNames.isEmpty()) {
                // No data found in JSON response, log message and return empty List
                LOG.info("No data found for" + uri);
                return new LinkedList<>();
            }

            return cityNames;

        } catch (Exception e) {
            // Log-Eintrag machen
            // return new ArrayList<Message>();
            LOG.error("Error occurred: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves detailed information about a specific city from the weather data provider.
     * <p>
     * This method sends a GET request to the weather data provider's API endpoint for the provided city name.
     * If the response status code is 200, it reads the JSON response body and extracts the city details.
     * The city details are then used to create a new City object.
     * If the response status code is not 200,
     * it logs an error message and returns a new City object with default values.
     * If the created City object is null after processing the JSON response,
     * it logs a message and returns a new City object with default values.
     * If an exception occurs during the execution of the method,
     * it logs an error message and throws a RuntimeException.
     *
     * @param cityName the name of the city for which to retrieve the details
     * @return a City object containing the details of the city, or a new City object with default values if no details are found or an error occurs
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public City readCityDetails(String cityName) {
        try {
            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata?city=" + cityName);
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            City city = new City();
            if (res.statusCode() == 200) {

                JsonNode node = mapper.readTree(res.body());

                int zip = node.get("city").get("zip").asInt();
                String data = node.get("data").asText();
                String[] parts = data.split("#");
                String country = parts[1].substring(8);

                city.setName(cityName);
                city.setZip(zip);
                city.setCountry(country);

            } else {
                // Log-Eintrag machen
                LOG.info("Error occurred, Status code: " + res.statusCode());
                return new City();
            }

            if (city.equals(null)) {
                // No data found in JSON response, log message and return empty List
                LOG.info("No data found for" + uri);
                return new City();
            }

            return city;

        } catch (Exception e) {
            // Log-Eintrag machen
            // return new ArrayList<Message>();
            LOG.error("Error occurred: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves detailed information about a list of cities from the weather data provider.
     * <p>
     * This method iterates over the provided list of city names
     * and sends a GET request to the weather data provider's API endpoint for each city name.
     * If the response status code is 200 for a city name,
     * it reads the JSON response body and extracts the city details.
     * The city details are then used to create a new City object,
     * which is added to a LinkedHashMap with the city's zip code as the key.
     * If the response status code is not 200 for a city name,
     * it logs an error message and returns an empty LinkedHashMap.
     * If the created LinkedHashMap is empty after processing all city names,
     * it logs a message and returns an empty LinkedHashMap.
     * If an exception occurs during the execution of the method,
     * it logs an error message and throws a RuntimeException.
     *
     * @param cityNames a LinkedList of city names for which to retrieve the details
     * @return a LinkedHashMap of City objects containing the details of the cities, with the city's zip code as the key, or an empty LinkedHashMap if no details are found or an error occurs
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public LinkedHashMap<Integer, City> readCityDetailsList(LinkedList<String> cityNames) {
        try {

            LinkedHashMap<Integer, City> cityMap = new LinkedHashMap<>();

            for (String cityName : cityNames) {
                URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata?city=" + cityName);
                HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", format).build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

                if (res.statusCode() == 200) {
                    JsonNode node = mapper.readTree(res.body());

                    int zip = node.get("city").get("zip").asInt();
                    String data = node.get("data").asText();
                    String[] parts = data.split("#");
                    String country = parts[1].substring(8);

                    City city = new City();
                    city.setName(cityName);
                    city.setZip(zip);
                    city.setCountry(country);

                    cityMap.put(zip, city);

                } else {
                    // Log-Eintrag machen
                    LOG.info("Error occurred, Status code: " + res.statusCode());
                    return new LinkedHashMap<Integer, City>();
                }

                if (cityMap.isEmpty()) {
                    // No data found in JSON response, log message and return empty List
                    LOG.info("No data found for" + uri);
                    return new LinkedHashMap<Integer, City>();
                }
            }

            return cityMap;

        } catch (
                Exception e) {
            // Log-Eintrag machen
            // return new ArrayList<Message>();
            LOG.error("Error occurred: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves detailed information about all cities from the weather data provider.
     * <p>
     * This method sends a GET request to the weather data provider's API endpoint for cities.
     * If the response status code is 200, it reads the JSON response body and extracts the city names and zip codes.
     * For each city, it sends another GET request to the weather data provider's API endpoint for the specific city.
     * If the response status code is 200, it reads the JSON response body and extracts the city details.
     * The city details are then used to create a new City object,
     * which is added to a LinkedHashMap with the city's zip code as the key.
     * If the response status code is not 200 for a city, it logs an error message and returns an empty LinkedHashMap.
     * If the created LinkedHashMap is empty after processing all cities,
     * it logs a message and returns an empty LinkedHashMap.
     * If an exception occurs during the execution of the method,
     * it logs an error message and throws a RuntimeException.
     *
     * @return a LinkedHashMap of City objects containing the details of the cities, with the city's zip code as the key, or an empty LinkedHashMap if no details are found or an error occurs
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public LinkedHashMap<Integer, City> readCities() {
        try {
            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata/cities/");
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            LinkedHashMap<Integer, City> cityMap = new LinkedHashMap<>();
            if (res.statusCode() == 200) {

                JsonNode node = mapper.readTree(res.body());
                for (JsonNode n : node) {

                    String name = n.get("name").asText();
                    String encodedCityName = name.replace(" ", "+");
                    int zip = n.get("zip").asInt();

                    uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata?city=" + encodedCityName);
                    req = HttpRequest.newBuilder(uri).GET().header("Accept", format).build();
                    res = client.send(req, HttpResponse.BodyHandlers.ofString());

                    if (res.statusCode() == 200) {
                        node = mapper.readTree(res.body());
                        String data = node.get("data").asText();
                        String[] parts = data.split("#");

                        City city = new City();
                        city.setName(name);
                        city.setZip(zip);

                        String country = parts[1].substring(8);
                        city.setCountry(country);

                        cityMap.put(zip, city);

                    } else {
                        // Log-Eintrag machen
                        LOG.info("Error occurred, Status code: " + res.statusCode());
                        return new LinkedHashMap<Integer, City>();
                    }
                }

                if (cityMap.isEmpty()) {
                    // No data found in JSON response, log message and return empty List
                    LOG.info("No data found for" + uri);
                    return new LinkedHashMap<Integer, City>();
                }

                return cityMap;

            } else {
                // Log-Eintrag machen
                LOG.info("Error occurred, Status code: " + res.statusCode());
                return new LinkedHashMap<Integer, City>();
            }

        } catch (Exception e) {
            // Log-Eintrag machen
            // return new ArrayList<Message>();
            LOG.error("Error occurred: " + e);
            throw new RuntimeException(e);
        }
    }

    /*----------------------------------------------WEATHER API REQUEST---------------------------------------------*/

    /**
     * Retrieves the current weather for a specific city from the weather data provider.
     * <p>
     * This method sends a GET request to the weather data provider's API endpoint for the provided city name.
     * If the response status code is 200, it reads the JSON response body and extracts the weather details.
     * The weather details are then used to create a new Weather object.
     * If the response status code is not 200,
     * it logs an error message and returns a new Weather object with default values.
     * If an exception occurs during the execution of the method,
     * it logs an error message and throws a RuntimeException.
     *
     * @param cityName the name of the city for which to retrieve the current weather
     * @return a Weather object containing the current weather of the city, or a new Weather object with default values if no details are found or an error occurs
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public Weather readCurrentWeatherByCity(String cityName) {
        try {

            String encodedCityName = cityName.replace(" ", "+");

            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata?city=" + encodedCityName);
            HttpRequest req = HttpRequest.newBuilder(uri).GET().header("Accept", format).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            LocalDateTime formatDateTime;
            if (res.statusCode() == 200) {

                Weather wetter = new Weather();

                JsonNode node = mapper.readTree(res.body());

                String data = node.get("data").asText();
                String[] parts = data.split("#");

                String dateTime = parts[0].substring(17);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                formatDateTime = LocalDateTime.parse(dateTime, format);
                wetter.setDTstamp(formatDateTime);

                String summery = parts[7].substring(16);
                wetter.setWeatherSummery(summery);

                String description = parts[8].substring(20);
                wetter.setWeatherDescription(description);

                double temp = Double.parseDouble(parts[9].substring(28));
                wetter.setCurrTempCelsius(temp);

                double pressure = Double.parseDouble(parts[10].substring(9));
                wetter.setPressure(pressure);

                double humidity = Double.parseDouble(parts[11].substring(9));
                wetter.setHumidity(humidity);

                double wind = Double.parseDouble(parts[12].substring(11));
                wetter.setWindSpeed(wind);

                double direction = Double.parseDouble(parts[13].substring(15));
                wetter.setWindDirection(direction);

                return wetter;

            } else {
                // Log-Eintrag machen
                LOG.info("Error occurred, Status code: " + res.statusCode());
                return new Weather();
            }

        } catch (Exception e) {
            // Log-Eintrag machen
            // return new ArrayList<Message>();
            LOG.error("Error occurred");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves weather data for a specific city and year from the weather data provider.
     * <p>
     * This method sends a GET request to the weather data provider's API endpoint for the provided city name and year.
     * If the response status code is 200, it reads the JSON response body and extracts the weather details.
     * The weather details are then used to create a new Weather object, which is added to a TreeMap with the date and time as the key.
     * If the response status code is not 200, it logs an error message and returns an empty TreeMap.
     * If the created TreeMap is empty after processing the JSON response, it logs a message and returns the empty TreeMap.
     * If an exception occurs during the execution of the method, it logs an error message and throws a RuntimeException.
     *
     * @param cityName the name of the city for which to retrieve the weather data
     * @param jahr     the year for which to retrieve the weather data
     * @return a TreeMap of Weather objects containing the weather data for the city and year, with the date and time as the key, or an empty TreeMap if no data is found or an error occurs
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public TreeMap<LocalDateTime, Weather> readWeatherByCityAndYear(String cityName, int jahr) {
        try {

            String encodedCityName = cityName.replace(" ", "+");

            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata/cityandyear?city=" + encodedCityName + "&year=" + jahr);
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();
            LocalDateTime formatDateTime;
            if (res.statusCode() == 200) {

                JsonNode node = mapper.readTree(res.body());
                for (JsonNode n : node) {

                    Weather weather = new Weather();

                    String data = n.get("data").asText();
                    String[] parts = data.split("#");

                    String dateTime = parts[0].substring(17);
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    formatDateTime = LocalDateTime.parse(dateTime, format);
                    weather.setDTstamp(formatDateTime);

                    String summery = parts[7].substring(16);
                    weather.setWeatherSummery(summery);

                    String description = parts[8].substring(20);
                    weather.setWeatherDescription(description);

                    double temp = Double.parseDouble(parts[9].substring(28));
                    weather.setCurrTempCelsius(temp);

                    double pressure = Double.parseDouble(parts[10].substring(9));
                    weather.setPressure(pressure);

                    double humidity = Double.parseDouble(parts[11].substring(9));
                    weather.setHumidity(humidity);

                    double wind = Double.parseDouble(parts[12].substring(11));
                    weather.setWindSpeed(wind);

                    double direction = Double.parseDouble(parts[13].substring(15));
                    weather.setWindDirection(direction);

                    weatherMap.put(formatDateTime, weather);
                }

                if (weatherMap.isEmpty()) {
                    // No data found in JSON response, log message and return empty List
                    LOG.info("No data found for" + uri);
                    return new TreeMap<>();
                }

                return weatherMap;

            } else {
                // Log-Eintrag machen
                LOG.info("Error occurred, Status code: " + res.statusCode());
                return new TreeMap<>();
            }

        } catch (Exception e) {
            // Log-Eintrag machen
            // return new ArrayList<Message>();
            LOG.error("Error occurred");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves weather data for a specific city and year from the weather data provider, filtered by the latest weather data.
     * <p>
     * This method sends a GET request to the weather data provider's API endpoint for the provided city name and year.
     * If the response status code is 200, it reads the JSON response body and extracts the weather details.
     * The weather details are then used to create a new Weather object, which is added to a TreeMap with the date and time as the key.
     * The TreeMap is then filtered to only include weather data after the provided latest weather data.
     * If the response status code is not 200, it logs an error message and returns an empty TreeMap.
     * If the created TreeMap is empty after processing the JSON response, it logs a message and returns the empty TreeMap.
     * If an exception occurs during the execution of the method, it logs an error message and throws a RuntimeException.
     *
     * @param cityName      the name of the city for which to retrieve the weather data
     * @param jahr          the year for which to retrieve the weather data
     * @param latestWeather the latest weather data to filter by
     * @return              a TreeMap of Weather objects containing the weather data for the city and year, filtered by the latest weather data, with the date and time as the key, or an empty TreeMap if no data is found or an error occurs
     * @throws RuntimeException if an exception occurs during the execution of the method
     */
    @Override
    public TreeMap<LocalDateTime, Weather> readWeatherByCityAndFilterByLatestWeather(String cityName, int jahr, LocalDateTime latestWeather) {
        try {
            // Encode the city name to be URL-friendly
            String encodedCityName = cityName.replace(" ", "+");

            // Create the URI for the GET request
            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata/cityandyear?city=" + encodedCityName + "&year=" + jahr);

            // Build and send the GET request
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            // Initialize the TreeMap to store the weather data
            TreeMap<LocalDateTime, Weather> weatherMap = new TreeMap<>();
            LocalDateTime formatDateTime;

            // If the response status code is 200, process the JSON response
            if (res.statusCode() == 200) {
                // Parse the JSON response
                JsonNode node = mapper.readTree(res.body());

                // Iterate over each node in the JSON response
                for (JsonNode n : node) {
                    // Create a new Weather object
                    Weather weather = new Weather();

                    // Extract the weather data from the node
                    String data = n.get("data").asText();
                    String[] parts = data.split("#");

                    // Parse the date and time
                    String dateTime = parts[0].substring(17);
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    formatDateTime = LocalDateTime.parse(dateTime, format);

                    // If the date and time is after the provided latest weather data, add the weather data to the TreeMap
                    if (formatDateTime.isAfter(latestWeather)) {
                        // Set the date and time
                        weather.setDTstamp(formatDateTime);

                        // Set the weather summary
                        String summery = parts[7].substring(16);
                        weather.setWeatherSummery(summery);

                        // Set the weather description
                        String description = parts[8].substring(20);
                        weather.setWeatherDescription(description);

                        // Set the temperature
                        double temp = Double.parseDouble(parts[9].substring(28));
                        weather.setCurrTempCelsius(temp);

                        // Set the pressure
                        double pressure = Double.parseDouble(parts[10].substring(9));
                        weather.setPressure(pressure);

                        // Set the humidity
                        double humidity = Double.parseDouble(parts[11].substring(9));
                        weather.setHumidity(humidity);

                        // Set the wind speed
                        double wind = Double.parseDouble(parts[12].substring(11));
                        weather.setWindSpeed(wind);

                        // Set the wind direction
                        double direction = Double.parseDouble(parts[13].substring(15));
                        weather.setWindDirection(direction);

                        // Add the Weather object to the TreeMap
                        weatherMap.put(formatDateTime, weather);
                    }
                }

                // If the TreeMap is empty after processing the JSON response, log a message and return the empty TreeMap
                if (weatherMap.isEmpty()) {
                    LOG.info("No data found for" + uri);
                    return new TreeMap<>();
                }

                // Return the TreeMap
                return weatherMap;

            } else {
                // If the response status code is not 200, log an error message and return an empty TreeMap
                LOG.info("Error occurred, Status code: " + res.statusCode());
                return new TreeMap<>();
            }

        } catch (Exception e) {
            // If an exception occurs during the execution of the method, log an error message and throw a RuntimeException
            LOG.error("Error occurred");
            throw new RuntimeException(e);
        }
    }
}