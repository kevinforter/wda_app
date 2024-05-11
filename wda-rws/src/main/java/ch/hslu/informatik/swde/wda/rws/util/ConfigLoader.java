package ch.hslu.informatik.swde.wda.rws.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigLoader {
    private Properties loadProperties() {
        Properties prop = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return null;
            }
            //load a properties file from class path
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }

    public boolean getInit() {
        Properties prop = loadProperties();
        return Boolean.parseBoolean(prop.getProperty("init"));
    }

    public void setInit(boolean initValue) {
        Properties prop = loadProperties();
        prop.setProperty("init", Boolean.toString(initValue));
        try (OutputStream output = new FileOutputStream(getClass().getClassLoader().getResource("config.properties").getFile())) {
            prop.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
