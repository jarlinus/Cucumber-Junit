package net.cucumber.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static final String CONFIGURATION_FILE_PATH = "configs/configuration.properties";
    private static final Configuration instance = new Configuration();

    private final Properties properties;

    private Configuration() {
        this.properties = loadProperties(CONFIGURATION_FILE_PATH);
    }

    private Properties loadProperties(String filePath) {
        Properties props = new Properties();
        
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalStateException("Configuration file not found: " + filePath);
            }
            
            props.load(inputStream);
            logger.info("Successfully loaded configuration from: {}", filePath);
            logger.debug("Loaded {} properties", props.size());
            
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load configuration from: " + filePath, e);
        }
        
        return props;
    }

    public static Configuration getInstance() {
        return instance;
    }

    /**
     * Gets a property given a key. Check in system properties first and defaults to properties file if not found.
     */
    public String getProperty(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Property key cannot be null or empty");
        }
        
        // Check system properties first
        String property = System.getProperty(key);
        
        // Fall back to properties file
        if (property == null) {
            property = properties.getProperty(key);
        }
        
        // Validate property value
        if (property == null) {
            logger.error("Required property '{}' is not set", key);
            throw new IllegalStateException(String.format("Required property %s is not set", key));
        }
        
        if (property.startsWith("${") && property.endsWith("}")) {
            logger.error("Property '{}' contains unresolved placeholder: {}", key, property);
            throw new IllegalStateException(String.format("Required property %s is not replaced (value: %s)", key, property));
        }
        
        logger.debug("Retrieved property '{}' = '{}'", key, property);
        return property.trim();
    }

    /**
     * Gets a property with a default value if the property is not found or invalid
     */
    public String getProperty(String key, String defaultValue) {
        try {
            return getProperty(key);
        } catch (IllegalStateException e) {
            logger.debug("Using default value for property '{}': {}", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets a boolean property value
     */
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        try {
            String value = getProperty(key);
            return Boolean.parseBoolean(value);
        } catch (IllegalStateException e) {
            logger.debug("Using default boolean value for property '{}': {}", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Gets an integer property value
     */
    public int getIntProperty(String key, int defaultValue) {
        try {
            String value = getProperty(key);
            return Integer.parseInt(value);
        } catch (IllegalStateException | NumberFormatException e) {
            logger.debug("Using default integer value for property '{}': {}", key, defaultValue);
            return defaultValue;
        }
    }

    /**
     * Check if a property exists
     */
    public boolean hasProperty(String key) {
        return System.getProperty(key) != null || properties.getProperty(key) != null;
    }
}
