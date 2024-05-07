/**
 * Autor: ChatGPT
 * Link: https://chat.openai.com/share/0b0bb792-e8f8-42b1-a813-5776ac7cde55
 * Version: 1.0.0
 */

package ch.hslu.informatik.swde.wda.rws.util;

import jakarta.ws.rs.ext.ParamConverter;

import java.time.LocalDateTime;

/**
 * This class implements the ParamConverter interface to convert a String into a LocalDateTime object and vice versa.
 * It is used for converting LocalDateTime objects to and from query parameters in JAX-RS applications.
 */
public class LocalDateTimeConverter implements ParamConverter<LocalDateTime> {

    /**
     * Default constructor.
     */

    /**
     * Converts a String into a LocalDateTime object.
     *
     * @param value The String to be converted into a LocalDateTime object.
     * @return The LocalDateTime object parsed from the input String, or null if the input is null.
     */
    @Override
    public LocalDateTime fromString(String value) {
        if (value == null) {
            return null;
        }
        return LocalDateTime.parse(value);
    }

    /**
     * Converts a LocalDateTime object into a String representation.
     *
     * @param value The LocalDateTime object to be converted into a String.
     * @return The String representation of the LocalDateTime object, or null if the input is null.
     */
    @Override
    public String toString(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}