package juja.slack.hitory.utils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class PropertiesHolder {

    private static Properties properties;

    public static Properties getProperties() {
        if (properties == null) {
            properties = loadProperties();
        }

        return properties;
    }

    public static String getProperty(String key) {
        return getProperties().getProperty(key);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try {
            //TODO smart props loading
            String propertyLocation = System.getProperty("property.location", "slack-history/src/main/resources/config.properties");
            System.out.println(new File("").getAbsolutePath());
            properties.load(new FileReader(propertyLocation));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load properties " + e.getMessage());
        }

        return properties;
    }
}