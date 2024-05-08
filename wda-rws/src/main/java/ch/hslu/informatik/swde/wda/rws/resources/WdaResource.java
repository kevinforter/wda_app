/**
 * This class defines the RESTful web service resource endpoints for the Weather Data Application (WDA).
 * It provides various endpoints for retrieving and manipulating data related to weather information,
 * cities, users, and more.
 * <p>
 * The endpoints are implemented as JAX-RS resource methods using annotations like @Path, @GET, @POST, @PUT, @DELETE, etc.
 * Each method corresponds to a specific operation or query related to the WDA functionality.
 * <p>
 * This class utilizes the WdaService interface to interact with the business logic of the WDA application.
 * It handles requests, performs necessary operations, and returns appropriate responses.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.rws.resources;

import ch.hslu.informatik.swde.wda.business.BusinessAPI;
import ch.hslu.informatik.swde.wda.business.BusinessImpl;
import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.reader.ApiReader;
import ch.hslu.informatik.swde.wda.reader.ApiReaderImpl;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;


/**
 * RESTful web service resource endpoints for the Weather Data Application (WDA).
 */
@Path("wda")
public class WdaResource {

    private static final String BASE_URI = "http://localhost:8080/wda/";

    private static final Logger LOG = LoggerFactory.getLogger(WdaResource.class);

    /**
     * Business-Komponente
     */

    private final BusinessAPI service = new BusinessImpl();


    /**
     * This method is used to add a city to the WDA application.
     *
     * @return A simple string message.
     */
    @POST
    @Path("cities")
    @Produces(MediaType.APPLICATION_JSON)
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
     * This method is used to get all cities from the WDA application.
     *
     * @return A simple string message.
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

    /**
     * This method is used to add thre current weather data to the WDA application.
     *
     * @param name The name of the city to be added
     */
    @POST
    @Path("weather/current")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCurrentWeatherOfCity(@QueryParam("name") String name) {

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
     * The city is specified by the client through a query parameter in the request.
     *
     * @param name The name of the city for which the latest weather data is to be retrieved. This is passed as a query parameter in the request.
     * @return A Response object containing the latest weather data for the specified city. The weather data is represented as a JSON object in the response body.
     * If the operation is successful, the HTTP status code of the response is 200 (OK).
     * If an error occurs during the operation, the HTTP status code of the response is 500 (Internal Server Error), and the response body contains a message describing the error.
     * @throws Exception If an error occurs while retrieving the weather data. The exception is logged and a response with status code 500 is returned.
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
     * This method is used to add weather data to the WDA application.
     *
     * @param year The year of the weather data to be added
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

//    /**
//     * This method is used to find a city by its name.
//     *
//     * @param name The name of the city to be added.
//     * @return A simple string message.
//     */
//    @GET
//    @Path("cities/name")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response findCityByName(@QueryParam("name") String name) {
//
//        City cityByName = service.findCityByName(name);
//
//        if (cityByName != null) {
//            return Response.ok(cityByName).build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }
//
//    /**
//     * This method is used to find a city by its id.
//     *
//     * @param id The id of the city to be added.
//     * @return A simple string message.
//     */
//    @GET
//    @Path("ortschaften/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response findCityById(@PathParam("id") int id) throws Exception {
//
//        Ortschaft ortschaftById = service.findCityById(id);
//
//        if (ortschaftById != null) {
//            return Response.ok(ortschaftById).build();
//        } else {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//    }
//

//
//    /**
//     * This method is used to get the weather data from a specific city and time span from the WDA application.
//     *
//     * @param id The id of the city data to be found
//     * @param von The date of the weather data to be found
//     * @param bis The date of the weather data to be found
//     * @return A simple string message.
//     */
//    @GET
//    @Path("wetterdaten/timespan")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response getWeatherFromCityByTimeSpan(@QueryParam("id") int id, @QueryParam("von") LocalDateTime von, @QueryParam("bis") LocalDateTime bis) throws Exception {
//
//        List<Wetter> wetterList = service.getWeatherFromCityByTimeSpan(id, von, bis);
//        return Response.ok(wetterList).build();
//    }
//
//    /**
//     * This method is used to add weather data to the WDA application.
//     *
//     * @param name The name of the city to be added
//     */
//    @POST
//    @Path("wetterdaten/wetter")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addWetter(@QueryParam("name") String name) throws Exception {
//
//        service.addWetter(name);
//
//        return Response.ok(service.latestWeatherByCity(service.findCityByName(name).getId())).build();
//    }
//
//    /**
//     * This method is used to add weather data to the WDA application.
//     *
//     * @param name The name of the city to be added
//     * @param jahr The year of the weather data to be added
//     */
//    @POST
//    @Path("wetterdaten/wetterjahr")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response addWetterJahr(@QueryParam("name") String name, @QueryParam("jahr") int jahr) throws Exception {
//
//        service.addWetterJahr(name, jahr);
//
//        return Response.noContent().build();
//    }
//
//    @POST
//    @Path("wetterdaten/wetter/alle")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response addAllWetter() throws Exception {
//
//        service.addAllWetter();
//
//        return Response.created(URI.create(BASE_URI + "wetterdaten/wetter/alle")).build();
//    }
}