/**
 * Diese Schnittstelle ergänzt die generische Persister-Schnittstelle
 * mit zusätzlichen Funktionalitäten für die Persistierung von Wetterdaten.
 *
 * @author Kevin
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.persister.DAO;

import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public interface WeatherDAO extends GenericDAO<Weather> {

    /**
     * @return Anzahl der Wetterdaten pro Stadt
     */
    long getNumberOfWeatherByCity(int cityId, String persistenceUnitName);

    /**
     * Holt das neueste Weather für eine bestimmte Ortschaft basierend auf der Ortschafts-ID.
     *
     * @param cityId Die ID der Ortschaft.
     * @return Das neueste Weather-Objekt für die angegebene Ortschaft; null, wenn keine Daten gefunden werden.
     */
    Weather findLatestWeatherByCity(int cityId, String persistenceUnitName);

    /**
     * Holt das älteste Weather für eine bestimmte Ortschaft basierend auf der Ortschafts-ID.
     *
     * @param cityId Die ID der Ortschaft.
     * @return Das neueste Weather-Objekt für die angegebene Ortschaft; null, wenn keine Daten gefunden werden.
     */
    Weather findOldestWeatherByCity(int cityId, String persistenceUnitName);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft zu einem bestimmten Zeitpunkt.
     *
     * @param date   Der spezifische Zeitpunkt.
     * @param cityId Die ID der Ortschaft.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    Weather findWeatherFromCityByDateTime(LocalDateTime date, int cityId, String persistenceUnitName);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft zu ein bestimmtes Jahr.
     *
     * @param year   Das spezifische Jahr.
     * @param cityId Die ID der Ortschaft.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    List<LocalDateTime> findWeatherDateFromCityByYear(int year, int cityId, String persistenceUnitName);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft zu ein bestimmtes Jahr.
     *
     * @param year   Das spezifische Jahr.
     * @param cityId Die ID der Ortschaft.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    TreeMap<LocalDateTime, Weather> findWeatherFromCityByYear(int year, int cityId, String persistenceUnitName);

    /**
     * Holt Wetterdaten für ein bestimmtes Jahr.
     *
     * @param year Das spezifische Jahr.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    TreeMap<LocalDateTime, Weather> findWeatherByYear(int year, String persistenceUnitName);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft zu ein bestimmtes Jahr.
     *
     * @param month  Der spezifische Monat.
     * @param cityId Die ID der Ortschaft.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    TreeMap<LocalDateTime, Weather> findWeatherFromCityByMonth(int month, int cityId, String persistenceUnitName);

    /**
     * Holt Wetterdaten für ein bestimmtes Jahr.
     *
     * @param days die spezifischen Tage.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    TreeMap<LocalDateTime, Weather> findWeatherByDayDifference(int days, String persistenceUnitName);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft innerhalb eines bestimmten Zeitraums.
     *
     * @param ortschaftId Die ID der Ortschaft.
     * @param von         Anfangsdatum des Zeitraums.
     * @param bis         Enddatum des Zeitraums.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    TreeMap<LocalDateTime, Weather> findWeatherFromCityByTimeSpan(int ortschaftId, LocalDateTime von, LocalDateTime bis, String persistenceUnitName);
//
//    /**
//     * Holt die minimale und maximale Temperatur für einen bestimmten Zeitpunkt.
//     *
//     * @param dateTime Der Zeitpunkt für die Abfrage.
//     * @return Eine Liste von Weather-Objekten mit minimalen und maximalen Temperaturen; leer, wenn keine Daten gefunden werden.
//     */
//    List<Weather> findMinMaxTemperatureByDateTime(LocalDateTime dateTime);
//
//    /**
//     * Holt die minimale und maximale Luftfeuchtigkeit für einen bestimmten Zeitpunkt.
//     *
//     * @param dateTime Der Zeitpunkt für die Abfrage.
//     * @return Eine Liste von Weather-Objekten mit minimalen und maximalen Luftfeuchtigkeitswerten; leer, wenn keine Daten gefunden werden.
//     */
//    List<Weather> findMinMaxHumidityByDateTime(LocalDateTime dateTime);
//
//    /**
//     * Holt den minimalen und maximalen Luftdruck für einen bestimmten Zeitpunkt.
//     *
//     * @param dateTime Der Zeitpunkt für die Abfrage.
//     * @return Eine Liste von Weather-Objekten mit minimalen und maximalen Luftdruckwerten; leer, wenn keine Daten gefunden werden.
//     */
//    List<Weather> findMinMaxPressureByDateTime(LocalDateTime dateTime);

    /**
     * Speichert alle Städte in der Map ab
     *
     * @param weatherMap mit allen Wetterdaten
     */
    void saveAllWeather(TreeMap<LocalDateTime, Weather> weatherMap, int cityId, String persistenceUnitName);

    /**
     * Gibt an obe eine Tabelle leer oder voll ist
     *
     * @param cityId die id der Stadt
     * @return true oder false
     */
    boolean ifWeatherOfCityExist(int cityId, String persistenceUnitName);
}
