package ch.hslu.informatik.swde.wda.persister.DAO;

import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Diese Schnittstelle ergänzt die generische Persister-Schnittstelle
 * mit zusätzlichen Funktionalitäten für die Persistierung von Wetterdaten.
 *
 * @author Kevin
 * @version 1.0
 */

public interface WeatherDAO extends GenericDAO<Weather> {

    /**
     *
     * @return Anzahl der Wetterdaten pro Stadt
     */
    long getNumberOfWeatherByCity(String cityName);

    /**
     * Holt das neueste Weather für eine bestimmte Ortschaft basierend auf der Ortschafts-ID.
     *
     * @param cityId Die ID der Ortschaft.
     * @return Das neueste Weather-Objekt für die angegebene Ortschaft; null, wenn keine Daten gefunden werden.
     */
    Weather findLatestWeatherByCity(int cityId);

    /**
     * Holt das älteste Weather für eine bestimmte Ortschaft basierend auf der Ortschafts-ID.
     *
     * @param cityId Die ID der Ortschaft.
     * @return Das neueste Weather-Objekt für die angegebene Ortschaft; null, wenn keine Daten gefunden werden.
     */
    Weather findOldestWeatherByCity(int cityId);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft zu einem bestimmten Zeitpunkt.
     *
     * @param date Der spezifische Zeitpunkt.
     * @param cityId Die ID der Ortschaft.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    Weather findWeatherFromCityByDateTime(LocalDateTime date, int cityId);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft zu ein bestimmtes Jahr.
     *
     * @param year Das spezifische Jahr.
     * @param cityId Die ID der Ortschaft.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    List<LocalDateTime> findWeatherFromCityByYear(int year, int cityId);

    /**
     * Holt Wetterdaten für eine bestimmte Ortschaft innerhalb eines bestimmten Zeitraums.
     *
     * @param ortschaftId Die ID der Ortschaft.
     * @param von Anfangsdatum des Zeitraums.
     * @param bis Enddatum des Zeitraums.
     * @return Eine Liste von Weather-Objekten; leer, wenn keine Daten gefunden werden.
     */
    List<Weather> findWeatherFromCityByTimeSpan(int ortschaftId, LocalDateTime von, LocalDateTime bis);

    /**
     * Holt die minimale und maximale Temperatur für einen bestimmten Zeitpunkt.
     *
     * @param dateTime Der Zeitpunkt für die Abfrage.
     * @return Eine Liste von Weather-Objekten mit minimalen und maximalen Temperaturen; leer, wenn keine Daten gefunden werden.
     */
    List<Weather> findMinMaxTemperatureByDateTime(LocalDateTime dateTime);

    /**
     * Holt die minimale und maximale Luftfeuchtigkeit für einen bestimmten Zeitpunkt.
     *
     * @param dateTime Der Zeitpunkt für die Abfrage.
     * @return Eine Liste von Weather-Objekten mit minimalen und maximalen Luftfeuchtigkeitswerten; leer, wenn keine Daten gefunden werden.
     */
    List<Weather> findMinMaxHumidityByDateTime(LocalDateTime dateTime);

    /**
     * Holt den minimalen und maximalen Luftdruck für einen bestimmten Zeitpunkt.
     *
     * @param dateTime Der Zeitpunkt für die Abfrage.
     * @return Eine Liste von Weather-Objekten mit minimalen und maximalen Luftdruckwerten; leer, wenn keine Daten gefunden werden.
     */
    List<Weather> findMinMaxPressureByDateTime(LocalDateTime dateTime);

    /**
     * Speichert alle Städte in der Map ab
     *
     * @param weatherMap mit allen Wetterdaten
     */
    void saveAllWeather(HashMap<LocalDateTime, Weather> weatherMap, String cityName);

    /**
     * Gibt an obe eine Tabelle leer oder voll ist
     *
     * @return true oder false
     */
    boolean ifWeatherOfCityExist(String cityName);
}
