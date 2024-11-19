package org.elyashevich.dbmigration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class PropertiesUtil {
    public static Properties loadProperties(final String filePath) {
        var properties = new Properties();
        try (var reader = Files.newBufferedReader(Path.of(filePath))) {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + filePath, e);
        }
        return properties;
    }
}