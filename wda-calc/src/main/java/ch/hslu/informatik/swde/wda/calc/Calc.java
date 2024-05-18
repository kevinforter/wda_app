package ch.hslu.informatik.swde.wda.calc;

import ch.hslu.informatik.swde.wda.domain.Weather;

import java.time.LocalDateTime;
import java.util.TreeMap;

/**
 * Diese Schnittstelle gibt die Funktionalitäten vor, die für die
 * Durchschnittsberechnungen benötigt werden.
 *
 * @author Jovan
 * @version 1.0
 */

public interface Calc {

    /**
     * Berechnet den Durchschnittswert der Temperatur aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den Durchschnittswert der Temperatur, gerundet auf zwei Dezimalstellen.
     */
    double getMeanForTemperature(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Berechnet den Durchschnittswert des Luftdrucks aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den Durchschnittswert des Luftdrucks, gerundet auf zwei Dezimalstellen.
     */
    double getMeanForPressure(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Berechnet den Durchschnittswert der Luftfeuchtigkeit aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den Durchschnittswert der Luftfeuchtigkeit, gerundet auf zwei Dezimalstellen.
     */
    double getMeanForHumidity(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Ermittelt den höchsten Temperaturwert aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den höchsten Temperaturwert.
     */
    double getMaxForTemperature(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Ermittelt den niedrigsten Temperaturwert aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den niedrigsten Temperaturwert.
     */
    double getMinForTemperature(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Ermittelt den höchsten Luftdruckwert aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den höchsten Luftdruckwert.
     */
    double getMaxForPressure(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Ermittelt den niedrigsten Luftdruckwert aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den niedrigsten Luftdruckwert.
     */
    double getMinForPressure(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Ermittelt den höchsten Luftfeuchtigkeitswert aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den höchsten Luftfeuchtigkeitswert.
     */
    double getMaxForHumidity(TreeMap<LocalDateTime, Weather> weatherMap);

    /**
     * Ermittelt den niedrigsten Luftfeuchtigkeitswert aus einer Liste von Wetterdaten.
     *
     * @param weatherMap Eine Liste von Wetter-Objekten.
     * @return Den niedrigsten Luftfeuchtigkeitswert.
     */
    double getMinForHumidity(TreeMap<LocalDateTime, Weather> weatherMap);
}
