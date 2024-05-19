/**
 * This class defines the RESTful web service resource endpoints for the Weather Data Application (WDA).
 * It provides various endpoints for retrieving and manipulating data related to weather information,
 * cities, users, and more.
 * <p>
 * The endpoints are implemented as JAX-RS resource methods using annotations like @Path, @GET, @POST, @PUT, @DELETE, etc.
 * Each method corresponds to a specific operation or query related to the WDA functionality.
 * <p>
 * This class uses the WdaService interface to interact with the business logic of the WDA application.
 * It handles requests, performs the necessary operations, and returns appropriate responses.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.rws.resources;

import ch.hslu.informatik.swde.wda.business.BusinessAPI;
import ch.hslu.informatik.swde.wda.business.BusinessImpl;
import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.*;


/**
 * RESTful web service resource endpoints for the Weather Data Application (WDA).
 */
@Path("wda")
public class WdaResource {

    private static final String puPROD = "postgresPU";

    private static final String BASE_URI = "http://localhost:8080/wda/";

    private static final Logger LOG = LoggerFactory.getLogger(WdaResource.class);

    /**
     * Business-Komponente
     */

    private final BusinessAPI service = new BusinessImpl(puPROD);

    /*----------------------------------------------CITY RESOURCES---------------------------------------------*/

    /**
     * Adds all cities to the Weather Data Application (WDA).
     * <p>
     * This method calls the addAllCities method of the service object,
     * which is an instance of the BusinessAPI interface.
     * If the operation is successful, it returns a Response object with an HTTP status code of 200 (OK).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @return a Response object with an HTTP status code of 200 (OK) if the operation is successful,
     * or a Response object with an HTTP status code of 500 (Internal Server Error)
     * and an entity containing a message describing the error if an exception occurs
     */
    @POST
    @Path("cities")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAllCities() {

        try {
            service.addAllCities();

            return Response.ok().build();
        } catch (Exception e) {
            LOG.error("Error while adding cities: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding cities")
                    .build();
        }
    }

    /**
     * Retrieves all cities from the Weather Data Application (WDA).
     * <p>
     * This method calls the getAllCities method of the service object,
     * which is an instance of the BusinessAPI interface.
     * If the operation is successful and the list of cities is not empty,
     * it returns a Response object with an HTTP status code of 200 (OK) and the list of cities as the entity.
     * If the list of cities is empty, it returns a Response object with an HTTP status code of 404 (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @return a Response object with an HTTP status code of 200 (OK) and the list of cities as the entity if the operation is successful and the list of cities is not empty,
     * a Response object with an HTTP status code of 404 (Not Found) if the list of cities is empty,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("cities")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCities() {

        try {
            List<City> cityList = service.getAllCities();

            if (!cityList.isEmpty()) {
                return Response.ok(cityList).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }

        } catch (Exception e) {
            LOG.error("Error while getting cities: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting cities")
                    .build();
        }
    }

    /**
     * Retrieves a city by its name from the Weather Data Application (WDA).
     * <p>
     * This method calls the getCityByName method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name.
     * If the operation is successful and a city with the provided name is found,
     * it returns a Response object with an HTTP status code of 200 (OK) and the city as the entity.
     * If no city with the provided name is found, it returns a Response object with an HTTP status code of 404
     * (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param name the name of the city to retrieve
     * @return a Response object with an HTTP status code of 200 (OK) and the city as the entity if the operation is successful and a city with the provided name is found,
     * a Response object with an HTTP status code of 404 (Not Found) if no city with the provided name is found,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("cities/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCityByName(@PathParam("name") String name) {

        try {

            City cityByName = service.getCityByName(name);

            if (cityByName != null) {
                return Response.ok(cityByName).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting cities: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting cities")
                    .build();
        }
    }

    /*----------------------------------------------WEATHER RESOURCES---------------------------------------------*/

    /**
     * Adds weather data for all cities and a specific year to the Weather Data Application (WDA).
     * <p>
     * This method retrieves a list of all cities from the service object,
     * which is an instance of the BusinessAPI interface.
     * It then calls the addWeatherOfCityByYear method of the service object for each city in the list with the provided year.
     * If the operation is successful, it returns a Response object with an HTTP status code of 200 (OK).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param year the year for which to add the weather data
     * @return a Response object with an HTTP status code of 200 (OK) if the operation is successful,
     * or a Response object with an HTTP status code of 500 (Internal Server Error)
     * and an entity containing a message describing the error if an exception occurs
     */
    @POST
    @Path("weather/{year}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAllWeather(@PathParam("year") int year) {

        try {
            List<City> cityList = service.getAllCities();

            for (City c : cityList) {
                service.addWeatherOfCityByYear(c.getName(), year);
            }

            return Response.ok().build();
        } catch (Exception e) {
            LOG.error("Error while adding weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }

    /**
     * Adds the current weather of a specific city to the Weather Data Application (WDA).
     * <p>
     * This method calls the addCurrentWeatherOfCity method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name.
     * If the operation is successful, it returns a Response object with an HTTP status code of 200 (OK).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param name the name of the city for which to add the current weather
     * @return a Response object with an HTTP status code of 200 (OK) if the operation is successful,
     * or a Response object with an HTTP status code of 500 (Internal Server Error)
     * and an entity containing a message describing the error if an exception occurs
     */
    @POST
    @Path("weather/current/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCurrentWeatherOfCity(@PathParam("name") String name) {

        try {
            service.addCurrentWeatherOfCity(name);

            return Response.ok().build();
        } catch (Exception e) {
            LOG.error("Error while adding weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }

    /**
     * Retrieves the current weather of a specific city from the Weather Data Application (WDA).
     * <p>
     * This method calls the getCurrentWeatherOfCity method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name.
     * If the operation is successful and the current weather of the city is found,
     * it returns a Response object with an HTTP status code of 200 (OK) and the current weather as the entity.
     * If no current weather of the city is found, it returns a Response object with an HTTP status code of 404
     * (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param name the name of the city for which to retrieve the current weather
     * @return a Response object with an HTTP status code of 200 (OK) and the current weather as the entity if the operation is successful and the current weather of the city is found,
     * a Response object with an HTTP status code of 404 (Not Found) if no current weather of the city is found,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("weather/current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentWeatherOfCity(@QueryParam("name") String name) {

        try {
            Weather currentWeather = service.getCurrentWeatherOfCity(name);

            if (currentWeather != null) {
                return Response.ok(currentWeather).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint that retrieves the latest weather data for a specific city.
     * The client specifies the city through a query parameter in the request.
     *
     * @param name The name of the city for which the latest weather data is to be retrieved. This is passed as a query parameter in the request.
     * @return A Response object containing the latest weather data for the specified city. The weather data is represented as a JSON object in the response body.
     * If the operation is successful, the HTTP status code of the response is 200 (OK).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error), and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/latest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestWeatherOfCity(@QueryParam("name") String name) {

        try {
            Weather latestWeather = service.getLatestWeatherOfCity(name);

            if (latestWeather != null) {
                return Response.ok(latestWeather).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * Adds weather data for a specific city and year to the Weather Data Application (WDA).
     * <p>
     * This method calls the addWeatherOfCityByYear method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name and year.
     * If the operation is successful, it returns a Response object with an HTTP status code of 200 (OK).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param year the year for which to add the weather data
     * @param name the name of the city for which to add the weather data
     * @return a Response object with an HTTP status code of 200 (OK) if the operation is successful,
     * or a Response object with an HTTP status code of 500 (Internal Server Error)
     * and an entity containing a message describing the error if an exception occurs
     */
    @POST
    @Path("weather/{year}/{name}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addWeatherOfCityByYear(@PathParam("year") int year, @PathParam("name") String name) {

        try {
            service.addWeatherOfCityByYear(name, year);

            return Response.ok().build();
        } catch (Exception e) {
            LOG.error("Error while adding weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }

    /**
     * Retrieves weather data for a specific city and year from the Weather Data Application (WDA).
     * <p>
     * This method calls the getWeatherOfCityByYear method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name and year.
     * If the operation is successful and the weather data is found,
     * it returns a Response object with an HTTP status code of 200 (OK) and the weather data as the entity.
     * If no weather data is found, it returns a Response object with an HTTP status code of 404 (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param year the year for which to retrieve the weather data
     * @param name the name of the city for which to retrieve the weather data
     * @return a Response object with an HTTP status code of 200 (OK) and the weather data as the entity if the operation is successful and the weather data is found,
     * a Response object with an HTTP status code of 404 (Not Found) if no weather data is found,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("weather/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherOfCityByYear(@PathParam("year") int year, @QueryParam("name") String name) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherOfCityByYear(year, name);

            if (!weatherMap.isEmpty()) {
                return Response.ok(weatherMap).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * Retrieves weather data for a specific year from the Weather Data Application (WDA).
     * <p>
     * This method calls the getWeatherOfCityByYear method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name and year.
     * If the operation is successful and the weather data is found,
     * it returns a Response object with an HTTP status code of 200 (OK) and the weather data as the entity.
     * If no weather data is found, it returns a Response object with an HTTP status code of 404 (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param year the year for which to retrieve the weather data
     * @return a Response object with an HTTP status code of 200 (OK) and the weather data as the entity if the operation is successful and the weather data is found,
     * a Response object with an HTTP status code of 404 (Not Found) if no weather data is found,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("weather")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherByYear(@QueryParam("year") int year) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherByYear(year);

            if (!weatherMap.isEmpty()) {
                return Response.ok(weatherMap).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint that retrieves weather data for a specific city within a given timespan.
     * The client specifies the city and the timespan through path and query parameters in the request.
     *
     * @param name The name of the city for which the weather data is to be retrieved. This is passed as a path parameter in the request.
     * @param von  The start of the timespan for which the weather data is to be retrieved. This is passed as a query parameter in the request.
     * @param bis  The end of the timespan for which the weather data is to be retrieved. This is passed as a query parameter in the request.
     * @return A Response object containing the weather data for the specified city within the given timespan. The weather data is represented as a TreeMap object in the response body.
     * If the operation is successful and weather data exists for the specified city and timespan, the HTTP status code of the response is 200 (OK).
     * If no weather data exists for the specified city and timespan, the HTTP status code of the response is 503 (Service Unavailable).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error), and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/timespan/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherByCityAndTimeSpan(@PathParam("name") String name, @QueryParam("von") LocalDateTime von, @QueryParam("bis") LocalDateTime bis) {

        try {
            TreeMap<LocalDateTime, Weather> weatherRes = service.getWeatherByCityAndTimeSpan(name, von, bis);

            if (!weatherRes.isEmpty()) {
                return Response.ok(weatherRes).build();
            } else {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
            }

        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * Retrieves weather data for a specific city and month from the Weather Data Application (WDA).
     * <p>
     * This method calls the getWeatherOfCityByYear method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name and year.
     * If the operation is successful and the weather data is found,
     * it returns a Response object with an HTTP status code of 200 (OK) and the weather data as the entity.
     * If no weather data is found, it returns a Response object with an HTTP status code of 404 (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param month the month for which to retrieve the weather data
     * @param name  the name of the city for which to retrieve the weather data
     * @return a Response object with an HTTP status code of 200 (OK) and the weather data as the entity if the operation is successful and the weather data is found,
     * a Response object with an HTTP status code of 404 (Not Found) if no weather data is found,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("weather/{year}/byCityAndMonth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherOfCityByMonth(@QueryParam("name") String name, @QueryParam("month") int month) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherOfCityByMonth(month, name);

            if (!weatherMap.isEmpty()) {
                return Response.ok(weatherMap).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint
     * that retrieves the minimum and maximum weather data for a specific city within a given month of the year.
     * The client specifies the city and the month through query parameters in the request.
     *
     * @param name  The name of the city for which the minimum and maximum weather data is to be retrieved.
     *              This is passed as a query parameter in the request.
     * @param month The month of the year for which the minimum and maximum weather data is to be retrieved.
     *              This is passed as a query parameter in the request.
     * @return A Response object
     * containing the minimum and maximum weather data for the specified city within the given month of the year.
     * The weather data is represented as a String object in the response body.
     * If the operation is successful and weather data exists for the specified city and month,
     * the HTTP status code of the response is 200
     * (OK).
     * If no weather data exists for the specified city and month, the HTTP status code of the response is 404 (Not Found).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error),
     * and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/{year}/byCityAndMonth/minmax")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMinMaxDataOfCityByMonth(@QueryParam("name") String name, @QueryParam("month") int month) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherOfCityByMonth(month, name);

            if (weatherMap.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
            String res = service.getWeatherMinMaxDataOfCity(weatherMap);

            if (!res.isEmpty()) {
                return Response.ok(res).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint
     * that retrieves the mean weather data for a specific city within a given month of the year.
     * The client specifies the city and the month through query parameters in the request.
     *
     * @param name  The name of the city for which the mean weather data is to be retrieved.
     *              This is passed as a query parameter in the request.
     * @param month The month of the year for which the mean weather data is to be retrieved.
     *              This is passed as a query parameter in the request.
     * @return A Response object containing the mean weather data for the specified city within the given month of the year.
     * The weather data is represented as a String object in the response body.
     * If the operation is successful and weather data exists for the specified city and month,
     * the HTTP status code of the response is 200
     * (OK).
     * If no weather data exists for the specified city and month, the HTTP status code of the response is 404 (Not Found).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error),
     * and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/{year}/byCityAndMonth/mean")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeanDataOfCityByMonth(@QueryParam("name") String name, @QueryParam("month") int month) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherOfCityByMonth(month, name);

            if (weatherMap.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
            String res = service.getWeatherMeanDataOfCity(weatherMap);

            if (!res.isEmpty()) {
                return Response.ok(res).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * Retrieves weather data for a specific city and week from the Weather Data Application (WDA).
     * <p>
     * This method calls the getWeatherOfCityByWeek method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided city name and year.
     * If the operation is successful and the weather data is found,
     * it returns a Response object with an HTTP status code of 200 (OK) and the weather data as the entity.
     * If no weather data is found, it returns a Response object with an HTTP status code of 404 (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param week the month for which to retrieve the weather data
     * @param name the name of the city for which to retrieve the weather data
     * @return a Response object with an HTTP status code of 200 (OK) and the weather data as the entity if the operation is successful and the weather data is found,
     * a Response object with an HTTP status code of 404 (Not Found) if no weather data is found,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("weather/{year}/byCityAndWeek")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherOfCityByWeek(@QueryParam("name") String name, @QueryParam("week") int week) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherOfCityByWeek(week, name);

            if (!weatherMap.isEmpty()) {
                return Response.ok(weatherMap).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint
     * that retrieves the minimum and maximum weather data for a specific city within a given week of the year.
     * The client specifies the city and the week through query parameters in the request.
     *
     * @param name The name of the city for which the minimum and maximum weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @param week The week of the year for which the minimum and maximum weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @return A Response object
     * containing the minimum and maximum weather data for the specified city within the given week of the year.
     * The weather data is represented as a String object in the response body.
     * If the operation is successful and weather data exists for the specified city and week,
     * the HTTP status code of the response is 200
     * (OK).
     * If no weather data exists for the specified city and week, the HTTP status code of the response is 404 (Not Found).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error),
     * and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/{year}/byCityAndWeek/minmax")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMinMaxDataOfCityByWeek(@QueryParam("name") String name, @QueryParam("week") int week) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherOfCityByWeek(week, name);
            if (weatherMap.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();

            String res = service.getWeatherMinMaxDataOfCity(weatherMap);

            if (!res.isEmpty()) {
                return Response.ok(res).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint
     * that retrieves the mean weather data for a specific city within a given week of the year.
     * The client specifies the city and the week through query parameters in the request.
     *
     * @param name The name of the city for which the mean weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @param week The week of the year for which the mean weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @return A Response object containing the mean weather data for the specified city within the given week of the year.
     * The weather data is represented as a String object in the response body.
     * If the operation is successful and weather data exists for the specified city and week,
     * the HTTP status code of the response is 200
     * (OK).
     * If no weather data exists for the specified city and week, the HTTP status code of the response is 404 (Not Found).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error),
     * and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/{year}/byCityAndWeek/mean")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMeanDataOfCityByWeek(@QueryParam("name") String name, @QueryParam("week") int week) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherOfCityByWeek(week, name);
            if (weatherMap.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();

            String res = service.getWeatherMeanDataOfCity(weatherMap);

            if (!res.isEmpty()) {
                return Response.ok(res).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while getting weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while getting weather")
                    .build();
        }
    }

    /**
     * Retrieves weather data for a specific number of days in the past from the Weather Data Application (WDA).
     * <p>
     * This method calls the getWeatherByDayDifference method of the service object,
     * which is an instance of the BusinessAPI interface, with the provided number of days.
     * If the operation is successful and the weather data is found,
     * it returns a Response object with an HTTP status code of 200 (OK) and the weather data as the entity.
     * If no weather data is found, it returns a Response object with an HTTP status code of 404 (Not Found).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @param days the number of days in the past for which to retrieve the weather data
     * @return a Response object with an HTTP status code of 200 (OK) and the weather data as the entity if the operation is successful and the weather data is found,
     * a Response object with an HTTP status code of 404 (Not Found) if no weather data is found,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @GET
    @Path("weather/past")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWeatherByDayDifference(@QueryParam("name") String name, @QueryParam("days") int days) {

        try {
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherByDayDifference(days, name);

            if (!weatherMap.isEmpty()) {
                return Response.ok(weatherMap).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.error("Error while adding weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint
     * that retrieves the minimum and maximum weather data for a specific city within a given number of past days.
     * The client specifies the city and the number of past days through query parameters in the request.
     *
     * @param name The name of the city for which the minimum and maximum weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @param days The number of past days for which the minimum and maximum weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @return A Response object
     * containing the minimum and maximum weather data for the specified city within the given number of past days.
     * The weather data is represented as a String object in the response body.
     * If the operation is successful and weather data exists for the specified city and number of past days,
     * the HTTP status code of the response is 200
     * (OK).
     * If no weather data exists for the specified city and number of past days, the HTTP status code of the response is 404
     * (Not Found).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error),
     * and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/past/minmax")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMinMaxDataOfCityByDayDifference(@QueryParam("name") String name, @QueryParam("days") int days) {

        try {
            // Retrieve the weather data for the specified city within the given number of past days
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherByDayDifference(days, name);
            if (weatherMap.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();

            // Calculate the minimum and maximum weather data from the retrieved weather data
            String res = service.getWeatherMinMaxDataOfCity(weatherMap);

            // If the minimum and maximum weather data is not empty, return it with an HTTP status code of 200 (OK)
            if (!res.isEmpty()) {
                return Response.ok(res).build();
            } else {
                // If the minimum and maximum weather data is empty, return an HTTP status code of 404 (Not Found)
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            // Log the error and return an HTTP status code of 500 (Internal Server Error) with a message describing the error
            LOG.error("Error while adding weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint
     * that retrieves the mean weather data for a specific city within a given number of past days.
     * The client specifies the city and the number of past days through query parameters in the request.
     *
     * @param name The name of the city for which the mean weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @param days The number of past days for which the mean weather data is to be retrieved.
     *             This is passed as a query parameter in the request.
     * @return A Response object containing the mean weather data for the specified city within the given number of past days.
     * The weather data is represented as a String object in the response body.
     * If the operation is successful and weather data exists for the specified city and number of past days,
     * the HTTP status code of the response is 200
     * (OK).
     * If no weather data exists for the specified city and number of past days, the HTTP status code of the response is 404
     * (Not Found).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error),
     * and the response body contains a message describing the error.
     */
    @GET
    @Path("weather/past/mean")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMinMeanOfCityByDayDifference(@QueryParam("name") String name, @QueryParam("days") int days) {

        try {
            // Retrieve the weather data for the specified city within the given number of past days
            TreeMap<LocalDateTime, Weather> weatherMap = service.getWeatherByDayDifference(days, name);
            if (weatherMap.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();

            // Calculate the mean weather data from the retrieved weather data
            String res = service.getWeatherMeanDataOfCity(weatherMap);

            // If the mean weather data is not empty, return it with an HTTP status code of 200 (OK)
            if (!res.isEmpty()) {
                return Response.ok(res).build();
            } else {
                // If the mean weather data is empty, return an HTTP status code of 404 (Not Found)
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            // Log the error and return an HTTP status code of 500 (Internal Server Error) with a message describing the error
            LOG.error("Error while adding weather: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }

    /*----------------------------------------------UTIL RESOURCES---------------------------------------------*/

    /**
     * Initializes the Weather Data Application (WDA) by adding all cities and their current year's weather data.
     * <p>
     * This method first calls the addAllCities method of the service object,
     * which is an instance of the BusinessAPI interface, to add all cities to the WDA.
     * It then retrieves a list of all cities from the service object,
     * and for each city in the list,
     * it calls the addWeatherOfCityByYear method of the service object with the city's name and the current year.
     * If the operation is successful, it returns a Response object with an HTTP status code of 200 (OK).
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error) and an entity containing a message describing the error.
     *
     * @return a Response object with an HTTP status code of 200 (OK) if the operation is successful,
     * or a Response object with an HTTP status code of 500 (Internal Server Error)
     * and an entity containing a message describing the error if an exception occurs
     */
    @POST
    @Path("init")
    public Response initApp() {

        try {
            boolean status = service.init();

            if (!status) {
                return Response.ok("Init executed").build();
            } else {
                return Response.status(418).entity("Init already used once 🥳").build();
            }
        } catch (Exception e) {
            LOG.error("Error while processing init: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }

    /**
     * This method is a RESTful web service endpoint that destroys the Weather Data Application (WDA).
     * It calls the destroy method of the service object, which is an instance of the BusinessAPI interface.
     * <p>
     * If the operation is successful,
     * it returns a Response object with an HTTP status code of 200 (OK)
     * and a message indicating the successful destruction of the application.
     * If the operation is unsuccessful, it returns a Response object with an HTTP status code of 418 (I'm a teapot),
     * indicating that the server refuses to brew coffee because it is a teapot.
     * If an exception occurs during the operation,
     * it logs an error message and returns a Response object with an HTTP status code of 500
     * (Internal Server Error),
     * and the response body contains a message describing the error.
     *
     * @return a Response object with an HTTP status code of 200 (OK) and a message indicating the successful destruction of the application if the operation is successful,
     * a Response object with an HTTP status code of 418 (I'm a teapot) if the operation is unsuccessful,
     * or a Response object with an HTTP status code of 500 (Internal Server Error) and an entity containing a message describing the error if an exception occurs
     */
    @DELETE
    @Path("destroy")
    public Response destroyApp() {

        try {
            boolean status = service.destroy();

            if (status) {
                return Response.ok("Bomb has been planted").build();
            } else {
                return Response.status(418).build();
            }
        } catch (Exception e) {
            LOG.error("Error while processing init: ", e);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error while adding weather")
                    .build();
        }
    }
}