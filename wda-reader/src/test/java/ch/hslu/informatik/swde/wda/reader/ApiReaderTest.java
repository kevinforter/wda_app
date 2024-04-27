package ch.hslu.informatik.swde.wda.reader;

import ch.hslu.informatik.swde.domain.Weather;
import ch.hslu.informatik.swde.domain.City;
import ch.hslu.informatik.swde.wda.reader.util.Util;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ApiReaderTest {


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
}