package ch.hslu.informatik.swde.wda.reader;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Diese Schnittstelle gibt die Funktionalitäten vor, welche für die Abfrage
 * von Wetterdaten und Ortschaften benötigt werden.
 *
 * @author Kevin
 * @version 1.0
 */

public interface ApiReader {

    /**
     * Liest eine Liste von Ortschaften von einem externen Webservice.
     *
     * @return Eine Liste von Ortschaft-Namen, die aus der Antwort des Webservice extrahiert wurden.
     */
    LinkedList<String> readCityNames();

    /**
     * Liest eine Liste von Ortschaften von einem externen Webservice.
     *
     * @return Ortschaft-Objekt, die aus der Antwort des Webservice extrahiert wurde.
     */
    City readCityDetails(String cityName);

    /**
     * Liest eine Liste von Ortschaften von einem externen Webservice.
     *
     * @return Eine LinkedHashMap von Ortschaft-Objekten, die aus der Antwort des Webservice extrahiert wurden.
     */
    LinkedHashMap<Integer, City> readCityDetailsList(LinkedList<String> cityNames);

    /**
     * Liest eine Liste von Ortschaften von einem externen Webservice.
     *
     * @return Eine LinkedHashMap von Ortschaft-Objekten, die aus der Antwort des Webservice extrahiert wurden.
     */
    LinkedHashMap<Integer, City> readCities();

    /**
     * Liest Wetterdaten für eine spezifische Ortschaft von einem externen Webservice.
     *
     * @param cityName Der Name der Ortschaft.
     * @return Ein Wetter-Objekt mit den Wetterdaten der angegebenen Ortschaft.
     */
    Weather readCurrentWeatherByCity(String cityName);

    /**
     * Liest Wetterdaten für eine spezifische Ortschaft und ein bestimmtes Jahr von einem externen Webservice.
     *
     * @param cityName Der Name der Ortschaft.
     * @param jahr Das spezifizierte Jahr.
     * @return Eine Liste von Wetter-Objekten, die den Wetterdaten für das angegebene Jahr und die Ortschaft entsprechen.
     */
    TreeMap<LocalDateTime, Weather> readWeatherByCityAndYear(String cityName, int jahr);
}
