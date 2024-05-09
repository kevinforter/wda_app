/**
 * Autor: ChatGPT
 * Link: https://chat.openai.com/share/0b0bb792-e8f8-42b1-a813-5776ac7cde55
 * Version: 1.0.0
 */

package ch.hslu.informatik.swde.wda.rws.util;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;

import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

/**
 * This class is a JAX-RS ParamConverterProvider that provides ParamConverter instances for LocalDateTime objects.
 * It is used to enable automatic conversion of query parameters to LocalDateTime objects and vice versa in JAX-RS applications.
 */
@Provider
public class LocalDateTimeConverterProvider implements ParamConverterProvider {

    /**
     * Default constructor.
     */

    /**
     * Gets a ParamConverter instance for the specified type.
     *
     * @param rawType The raw type for which a ParamConverter is requested.
     * @param genericType The generic type for which a ParamConverter is requested.
     * @param annotations An array of annotations associated with the parameter.
     * @param <T> The type of the ParamConverter.
     * @return A ParamConverter instance for the specified type, or null if it is not supported.
     */
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.equals(LocalDateTime.class)) {
            return (ParamConverter<T>) new LocalDateTimeConverter();
        }
        return null;
    }
}
