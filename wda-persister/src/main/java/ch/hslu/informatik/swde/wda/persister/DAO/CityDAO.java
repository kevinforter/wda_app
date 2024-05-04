package ch.hslu.informatik.swde.wda.persister.DAO;

import ch.hslu.informatik.swde.wda.domain.City;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Diese Schnittstelle ergänzt die generische Persister-Schnittstelle
 * mit zusätzlichen Funktionalitäten für die Persistierung von Ortschaften.
 *
 * @author Kevin Forter
 * @version 1.0
 */

public interface CityDAO extends GenericDAO<City> {

    /**
     * Sucht und liefert eine City anhand ihres Namens.
     *
     * @param cityName Der Name der zu suchenden City.
     * @return eine City ID, falls Stadt mit diesem Namen gefunden wurde,
     * ansonsten wird 0 zurückgegeben
     */
    int findCityIdByName(String cityName);

    /**
     * Sucht und liefert eine City anhand ihres Namens.
     *
     * @param cityName Der Name der zu suchenden City.
     * @return Ein City-Objekt, falls eine City mit dem angegebenen Namen gefunden wurde
     * andernfalls leeres City-Objekt.
     */
    City findCityByName(String cityName);

    /**
     * Sucht nach einem Städtenamen
     *
     * @param cityName Der Name der zu suchenden City.
     * @return true oder false, falls Stadt schon existiert
     */
    boolean cityExists(String cityName);

    /**
     * Sucht nach einem Städtenamen
     *
     * @return eine Liste aller City Namen
     */
    List<String> allCityNames();

    /**
     * Speichert alle Städte in der Map ab
     *
     * @param cityMap mit allen Städten
     */
    void saveAllCities(LinkedHashMap<Integer, City> cityMap);


    /**
     * Gibt an obe eine Tabelle leer oder voll ist
     *
     * @return true oder false
     */
    boolean ifTableExist();
}
