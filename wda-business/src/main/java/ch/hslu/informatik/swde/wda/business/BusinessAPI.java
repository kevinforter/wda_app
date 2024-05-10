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

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public interface BusinessAPI {

    /**
     * Fügt alle Städte zur Datenbank hinzu.
     */
    void addAllCities();

    /**
     * Fügt das aktuelle Wetter einer bestimmten Stadt zur Datenbank hinzu.
     *
     * @param cityName Der Name der Stadt.
     */
    void addCurrentWeatherOfCity(String cityName);

    /**
     * Fügt das Wetter einer bestimmten Stadt für ein bestimmtes Jahr zur Datenbank hinzu.
     *
     * @param city Der Name der Stadt.
     * @param year Das Jahr, für das die Wetterdaten hinzugefügt werden sollen.
     */
    void addWeatherOfCityByYear(String city, int year);

    /**
     * Ruft eine Stadt nach ihrem Namen ab.
     *
     * @param name Der Name der Stadt.
     * @return Das City-Objekt, das dem gegebenen Namen entspricht.
     */
    City getCityByName(String name);

    /**
     * Ruft alle Städte aus der Datenbank ab.
     *
     * @return Eine Liste aller City-Objekte in der Datenbank.
     */
    List<City> getAllCities();

    /**
     * Ruft das aktuelle Wetter einer bestimmten Stadt ab.
     *
     * @param cityName Der Name der Stadt.
     * @return Das Weather-Objekt, das das aktuelle Wetter der Stadt darstellt.
     */
    Weather getCurrentWeatherOfCity(String cityName);

    /**
     * Ruft die neuesten Wetterdaten einer bestimmten Stadt ab.
     *
     * @param cityName Der Name der Stadt.
     * @return Das Weather-Objekt, das das neueste Wetter der Stadt darstellt.
     */
    Weather getLatestWeatherOfCity(String cityName);

    /**
     * Ruft das Wetter einer bestimmten Stadt für ein bestimmtes Jahr ab.
     *
     * @param year     Das Jahr, für das die Wetterdaten abgerufen werden sollen.
     * @param cityName Der Name der Stadt.
     * @return Ein TreeMap, bei dem die Schlüssel die Daten und Zeiten der Wetterdaten sind und die Werte die entsprechenden Weather-Objekte.
     */
    TreeMap<LocalDateTime, Weather> getWeatherOfCityByYear(int year, String cityName);
}
