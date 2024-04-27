package ch.hslu.informatik.swde.wda.reader;

import ch.hslu.informatik.swde.domain.City;
import ch.hslu.informatik.swde.domain.Weather;
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

    @Override
    public Weather readCurrentWeatherByCity(String name) {
        try {

            String encodedCityName = name.replace(" ", "+");

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

    @Override
    public LinkedHashMap<LocalDateTime, Weather> readWeatherByCityAndYear(String ortschaft, int jahr) {
        try {

            String encodedCityName = ortschaft.replace(" ", "+");

            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata/cityandyear?city=" + encodedCityName + "&year=" + jahr);
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            LinkedHashMap<LocalDateTime, Weather> weatherMap = new LinkedHashMap<>();
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

                    weatherMap.put(formatDateTime ,weather);
                }

                if (weatherMap.isEmpty()) {
                    // No data found in JSON response, log message and return empty List
                    LOG.info("No data found for" + uri);
                    return new LinkedHashMap<>();
                }

                return weatherMap;

            } else {
                // Log-Eintrag machen
                LOG.info("Error occurred, Status code: " + res.statusCode());
                return new LinkedHashMap<>();
            }

        } catch (Exception e) {
            // Log-Eintrag machen
            // return new ArrayList<Message>();
            LOG.error("Error occurred");
            throw new RuntimeException(e);
        }
    }

}