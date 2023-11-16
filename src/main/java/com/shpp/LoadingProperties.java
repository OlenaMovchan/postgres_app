package com.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LoadingProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingProperties.class);
    public Properties properties;

    public LoadingProperties() {
        properties = new Properties();
        if (!loadPropertiesFromExternalFile()) {
            loadProperties();
        }
    }

    private boolean loadPropertiesFromExternalFile() {
        String externalFilePath = "config/config.properties";
        try (FileInputStream input = new FileInputStream(externalFilePath)) {
            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            LOGGER.info("External file loaded from: " + externalFilePath);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean loadProperties() {
        String fileName = "config.properties";
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream input = classLoader.getResourceAsStream(fileName)) {
            properties.load(input);
            LOGGER.info("External file loaded");
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            LOGGER.error("The file does not exist");
        }
        return false;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}