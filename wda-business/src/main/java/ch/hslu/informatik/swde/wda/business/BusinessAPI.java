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

    /**
     * Ruft das Wetter einer bestimmten Stadt für einen bestimmten Monat ab.
     *
     * @param month     Der Monat, für das die Wetterdaten abgerufen werden sollen.
     * @param cityName Der Name der Stadt.
     * @return Ein TreeMap, bei dem die Schlüssel die Daten und Zeiten der Wetterdaten sind und die Werte die entsprechenden Weather-Objekte.
     */
    TreeMap<LocalDateTime, Weather> getWeatherOfCityByMonth(int month, String cityName);

    /**
     * Ruft das Wetter einer bestimmten Stadt für eine bestimmte Woche ab.
     *
     * @param week    Die Woche, für das die Wetterdaten abgerufen werden sollen.
     * @param cityName Der Name der Stadt.
     * @return Ein TreeMap, bei dem die Schlüssel die Daten und Zeiten der Wetterdaten sind und die Werte die entsprechenden Weather-Objekte.
     */
    TreeMap<LocalDateTime, Weather> getWeatherOfCityByWeek(int week, String cityName);

    /**
     * Ruft das Wetter für ein bestimmtes Jahr ab.
     *
     * @param year Das Jahr, für das die Wetterdaten abgerufen werden sollen.
     * @return Ein TreeMap, bei dem die Schlüssel die Daten und Zeiten der Wetterdaten sind und die Werte die entsprechenden Weather-Objekte.
     */
    TreeMap<LocalDateTime, Weather> getWeatherByYear(int year);

    /**
     * Retrieves a map of Weather entities within a specific number of days from the current date.
     * <p>
     * This method is used to fetch weather data for a specific number of days from the current date.
     * The returned map contains the timestamp of the weather data as the key and the corresponding Weather entity as the value.
     * The map is sorted in ascending order of the timestamp.
     *
     * @param days the number of days from the current date for which the Weather entities are to be retrieved
     * @return a TreeMap of Weather entities within the specified number of days from the current date, sorted in ascending order of the timestamp
     */
    TreeMap<LocalDateTime, Weather> getWeatherByDayDifference(int days);

    /**
     * Retrieves a map of Weather entities associated with a specific city and within a specific time span.
     * <p>
     * This method is used to fetch weather data for a specific city within a specific time span.
     * The returned map contains the timestamp of the weather data as the key and the corresponding Weather entity as the value.
     * The map is sorted in ascending order of the timestamp.
     *
     * @param cityName  the Name of the city for which the Weather entities are to be retrieved
     * @param von       the start of the time span for which the Weather entities are to be retrieved
     * @param bis       the end of the time span for which the Weather entities are to be retrieved
     * @return          a TreeMap of Weather entities associated with the provided city ID and within the specified time span, sorted in ascending order of the timestamp
     */
    TreeMap<LocalDateTime, Weather> getWeatherByCityAndTimeSpan(String cityName, LocalDateTime von, LocalDateTime bis);

    /**
     * Initializes the Weather Data Application (WDA) by adding all cities and their current year's weather data.
     * <p>
     * This method first calls the addAllCities method of the service object,
     * which is an instance of the BusinessAPI interface, to add all cities to the WDA.
     * It then retrieves a list of all cities from the service object,
     * and for each city in the list,
     * it calls the addWeatherOfCityByYear method of the service object with the city's name and the current year.
     */
    boolean init();

    /**
     * Löscht alle Tabellen
     */
    boolean destroy();
}
