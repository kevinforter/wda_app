/**
 * Diese Schnittstelle gibt die Hauptfunktionalitäten vor, die für die
 * Persistierung von Objekten benötigt werden.
 *
 * @author Kevin Forter
 * @version 1.0
 */

package ch.hslu.informatik.swde.wda.persister.DAO;

import java.util.List;

public interface GenericDAO<T> {

    /**
     * Fügt das übergebene Objekt dem Datenbestand hinzu.
     *
     * @param obj Das zu speichernde Objekt vom Typ T.
     */
    void speichern(T obj, String persistenceUnitName);

    /**
     * Entfernt das übergebene Objekt aus dem Datenbestand.
     *
     * @param id Die eindeutige ID des zu löschenden Objekts.
     */
    void loeschen(int id, String persistenceUnitName);

    /**
     * Aktualisiert das übergebene Objekt im Datenbestand.
     *
     * @param obj Das zu aktualisierende Objekt vom Typ T.
     */
    void aktualisieren(T obj, String persistenceUnitName);

    /**
     * Sucht und liefert ein Objekt vom Typ T, basierend auf seiner eindeutigen ID.
     *
     * @param id Die eindeutige ID des zu suchenden Objekts.
     * @return Das gefundene Objekt vom Typ T, oder ein leeres Objekt,
     * falls kein Objekt mit der angegebenen ID gefunden wurde.
     */
    T findById(int id, String persistenceUnitName);

    /**
     * Sucht und liefert ein Objekt vom Typ T, basierend auf dem Feldnamen und Wert.
     *
     * @param fieldName der Feldname wo sich value befindet.
     * @param value der Wert nach dem Gesucht wird
     * @return Das gefundene Objekt vom Typ T, oder ein leeres Objekt,
     * falls kein Objekt mit der angegebenen Parameter gefunden wurde.
     */
    T findEntityByFieldAndString(String fieldName, Object value, String persistenceUnitName);

    /**
     * Holt alle Objekte vom Typ T aus dem Datenbestand.
     *
     * @return Eine Liste aller Objekte vom Typ T im Datenbestand.
     * Die Liste kann leer sein, falls keine Objekte gefunden wurden.
     */
    List<T> alle(String persistenceUnitName);

    /**
     * Gibt an obe eine Tabelle leer oder voll ist
     *
     * @return true oder false
     */
    boolean ifTableExist(String persistenceUnitName);
}
