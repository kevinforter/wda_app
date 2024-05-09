/**
 * Diese Schnittstelle gibt die Funktionalitäten vor, welche für die Abfrage
 * von Wetterdaten und Ortschaften benötigt werden.
 *
 * @author Kevin
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.reader;

import ch.hslu.informatik.swde.wda.domain.City;
import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public interface ApiReader {

    /**
     * Liest eine Liste von Städtenamen von einem externen Wetterdienst.
     *
     * @return Eine LinkedList von Städtenamen, die aus der Antwort des Wetterdienstes extrahiert wurden.
     */
    LinkedList<String> readCityNames();

    /**
     * Liest die Details einer bestimmten Stadt von einem externen Wetterdienst.
     *
     * @param cityName Der Name der Stadt.
     * @return Ein City-Objekt, das aus der Antwort des Wetterdienstes extrahiert wurde.
     */
    City readCityDetails(String cityName);

    /**
     * Liest die Details einer Liste von Städten von einem externen Wetterdienst.
     *
     * @param cityNames Eine LinkedList von Städtenamen.
     * @return Eine LinkedHashMap von City-Objekten, mit der Stadt-ID als Schlüssel, die aus der Antwort des Wetterdienstes extrahiert wurden.
     */
    LinkedHashMap<Integer, City> readCityDetailsList(LinkedList<String> cityNames);

    /**
     * Liest die Details aller Städte von einem externen Wetterdienst.
     *
     * @return Eine LinkedHashMap von City-Objekten, mit der Stadt-ID als Schlüssel, die aus der Antwort des Wetterdienstes extrahiert wurden.
     */
    LinkedHashMap<Integer, City> readCities();

    /**
     * Liest die aktuellen Wetterdaten für eine bestimmte Stadt von einem externen Wetterdienst.
     *
     * @param cityName Der Name der Stadt.
     * @return Ein Weather-Objekt, das die aktuellen Wetterdaten der angegebenen Stadt enthält.
     */
    Weather readCurrentWeatherByCity(String cityName);

    /**
     * Liest die Wetterdaten für eine bestimmte Stadt und ein bestimmtes Jahr von einem externen Wetterdienst.
     *
     * @param cityName Der Name der Stadt.
     * @param jahr     Das angegebene Jahr.
     * @return Ein TreeMap von Weather-Objekten, mit dem Datum und der Uhrzeit als Schlüssel, die die Wetterdaten für das angegebene Jahr und die Stadt darstellen.
     */
    TreeMap<LocalDateTime, Weather> readWeatherByCityAndYear(String cityName, int jahr);
}
