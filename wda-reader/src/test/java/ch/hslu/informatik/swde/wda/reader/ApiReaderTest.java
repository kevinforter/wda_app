package ch.hslu.informatik.swde.wda.reader;

import ch.hslu.informatik.swde.wda.domain.Weather;
import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.reader.util.Util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ApiReaderTest {

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String BASE_URI = "http://eee-03318.simple.eee.intern:8080/";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String format = "application/json";


    /*-----------------------------------------------CITY API REQUEST-----------------------------------------------*/
    @Nested
    class CityTest {
        @Tag("unittest")
        @Test
        void test_GetCityNames_ShouldBe40Cities() {

            ApiReader proxy = new ApiReaderImpl();

            LinkedList<String> resNames = proxy.readCityNames();
            assertNotNull(resNames);
            assertEquals(40, resNames.size(), "Nicht alle Städte vorhanden");
        }

        @Tag("unittest")
        @Test
        void test_GetDetailsPerCity_ShouldBe40CitiesWithCountryCodeCH() {

            ApiReader proxy = Mockito.mock(ApiReaderImpl.class);

            LinkedList<String> mockCityNames = IntStream.range(0, 40)
                    .mapToObj(i -> "City" + i)
                    .collect(Collectors.toCollection(LinkedList::new));
            when(proxy.readCityNames()).thenReturn(mockCityNames);

            City mockCity = new City();
            mockCity.setCountry("CH");
            when(proxy.readCityDetails(Mockito.anyString())).thenReturn(mockCity);

            for (String cityName : mockCityNames) {
                City city = proxy.readCityDetails(cityName);
                assertEquals("CH", city.getCountry(), "Country Code CH fehlt");
            }
        }

        @Tag("unittest")
        @Test
        void test_GetCityDetailsList_ShouldBe40CitiesWithCountryCodeCH() {

            ApiReader proxy = Mockito.mock(ApiReaderImpl.class);

            LinkedList<String> mockCityNames = IntStream.range(0, 40)
                    .mapToObj(i -> "City" + i)
                    .collect(Collectors.toCollection(LinkedList::new));
            when(proxy.readCityNames()).thenReturn(mockCityNames);

            LinkedHashMap<Integer, City> resDetails = proxy.readCityDetailsList(mockCityNames);

            for (City c : resDetails.values()) {
                assertEquals("CH", c.getCountry(), "Country Code CH fehlt");
            }
        }

        @Tag("unittest")
        @Test
        void test_GetCitiesObjects_ShouldBe40Cities() {

            ApiReader proxy = new ApiReaderImpl();

            LinkedHashMap<Integer, City> resOrt = proxy.readCities();
            assertNotNull(resOrt);
            assertEquals(40, resOrt.size(), "Nicht alle Städte vorhanden");
        }
    }

    /*----------------------------------------------WEATHER API REQUEST---------------------------------------------*/
    @Nested
    class WeatherTest {
        @Tag("unittest")
        @ParameterizedTest
        @MethodSource("cityListProvider")
        void test_GetCurrentWeather_ShouldReturnNewestWeather(LinkedList<String> cityList) {

            ApiReader proxy = new ApiReaderImpl();

            for (String cityName : cityList) {
                Weather resWeather = proxy.readCurrentWeatherByCity(cityName);
                assertNotNull(resWeather, "Liste ist leer");
            }
        }

        @Tag("unittest")
        @ParameterizedTest
        @MethodSource("cityListProvider")
        void test_GetWeatherByCityAndYear_ShouldReturnListOfWeatherDataOfGivenYear(LinkedList<String> cityList) {

            ApiReader proxy = new ApiReaderImpl();

            for (String cityName : cityList) {
                LinkedHashMap<LocalDateTime, Weather> resWeather = proxy.readWeatherByCityAndYear(cityName, 2024);
                assertNotNull(resWeather, "Liste ist leer");
            }
        }

        static Stream<LinkedList<String>> cityListProvider() {
            LinkedList<String> cities = Util.createCities();
            return Stream.of(cities);
        }


    }

    /*----------------------------------------------API STATUS TEST---------------------------------------------*/
    @Nested
    class apiTest {

        @Tag("unittest")
        @Test
        public void allCitiesExist_whenCitiesAreRetrieved_then200IsReceived()
                throws IOException, InterruptedException {

            // Given
            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata/cities/");
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();

            // When
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            // Then
            assertEquals(
                    200, res.statusCode(), "Should be Code 200"
            );
        }

        @Tag("unittest")
        @Test
        public void givenCityDoesNotExists_whenWeatherDataIsRetrieved_then500IsReceived()
                throws IOException, InterruptedException {

            // Given
            String cityName = "Berlin";
            URI uri = URI.create(BASE_URI + "weatherdata-provider/rest/weatherdata?city=" + cityName);
            HttpRequest req = HttpRequest.newBuilder(uri).header("Accept", format).build();

            // When
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());

            // Then
            assertEquals(
                    500, res.statusCode(), "Should be Code 500"
            );
        }
    }
}