package com.qa.salesforce.config;

import com.qa.salesforce.exceptions.FrameworkException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final Logger LOG = LogManager.getLogger(ConfigManager.class);
    private static final String ENV_KEY = "env";
    private static final String DEFAULT_ENV = "qa";
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final Properties properties = new Properties();
    private final String environment;

    private ConfigManager() {
        load(FrameworkConstants.BASE_CONFIG_RESOURCE);
        this.environment = System.getProperty(ENV_KEY, properties.getProperty(ENV_KEY, DEFAULT_ENV)).trim().toLowerCase();
        load(String.format(FrameworkConstants.ENV_CONFIG_RESOURCE_TEMPLATE, environment));
        LOG.info("Configuration initialised for environment '{}'", environment);
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    private void load(String classpathResource) {
        try (InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (inputStream == null) {
                throw new FrameworkException("Configuration file not found on classpath: " + classpathResource);
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new FrameworkException("Unable to load configuration file: " + classpathResource, e);
        }
    }

    public String get(String key) {
        String value = System.getProperty(key, properties.getProperty(key));
        if (value == null) {
            throw new FrameworkException("Missing configuration key: '" + key + "' for environment '" + environment + "'");
        }
        return value.trim();
    }

    public String get(String key, String defaultValue) {
        String value = System.getProperty(key, properties.getProperty(key, defaultValue));
        return value == null ? null : value.trim();
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public String getEnvironment() {
        return environment;
    }
}
