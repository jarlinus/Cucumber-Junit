package net.cucumber.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration for the Functional Tests
 * <p>
 * Expects to find the properties in classpath:configs/configuration.properties
 * A singleton static instance is created at startup time.
 * Supports overriding the properties found in the file by System Properties of the same name
 */
public class Configuration {
    private final String CONFIGURATION_FILE_PATH = "configs/configuration.properties";
    private static Configuration instance = new Configuration();

    private Properties properties;

    public Configuration() {
        properties = loadProperties(CONFIGURATION_FILE_PATH);
    }

    private Properties loadProperties(String filePath) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filePath).getFile());
            try (InputStream inputStream = new FileInputStream(file)) {
                Properties properties = new Properties();
                properties.load(inputStream);
                return properties;
            }
        } catch (IOException e) {
            throw new RuntimeException("Configuration.properties not found at " + filePath, e);
        }
    }

    public static Configuration getInstance() {
        return instance;
    }

    /**
     * Gets a property given a key. Check in system properties first and defaults to properties file if not found.
     */
    public String getProperty(String key) throws IllegalStateException {
        String property = System.getProperty(key);
        if (property == null) {
            property = properties.getProperty(key);
        }
        if (property == null) {
            throw new IllegalStateException(String.format("Required property %s is not set", key));
        } else if (property.startsWith("${")) {
            throw new IllegalStateException(String.format("Required property %s is not replaced (value: %s)", key, property));
        } else {
            return property;
        }
    }

    public String getProperty(String key, String defaultValue) {
        try {
            return getProperty(key);
        } catch (IllegalStateException e) {
            return defaultValue;
        }

    }
}
