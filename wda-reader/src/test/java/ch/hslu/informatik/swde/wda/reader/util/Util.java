package ch.hslu.informatik.swde.wda.reader.util;

import ch.hslu.informatik.swde.wda.reader.*;
import java.util.LinkedList;
public class Util {

    private Util() {

    }

    public static LinkedList<String> createCities() {

        ApiReader proxy = new ApiReaderImpl();

        return proxy.readCityNames();
    }
}
