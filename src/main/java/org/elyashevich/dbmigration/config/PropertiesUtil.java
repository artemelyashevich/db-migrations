package org.elyashevich.dbmigration.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.domain.DatabaseProperties;
import org.elyashevich.dbmigration.validation.Validation;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertiesUtil {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();

    private final static String DRIVER_PROPERTY = "migrations.db.driver";
    private final static String USERNAME_PROPERTY = "migrations.db.username";
    private final static String PASSWORD_PROPERTY = "migrations.db.password";
    private final static String URL_PROPERTY = "migrations.db.url";

    private final Validation<DatabaseProperties> validation;

    public PropertiesUtil(Validation<DatabaseProperties> validation) {
        this.validation = validation;
    }

    public DatabaseProperties loadProperties(final String filename) {
        DatabaseProperties properties = null;
        try {
            var resource = ResourceBundle.getBundle(filename);
            properties = new DatabaseProperties(
                    resource.getString(DRIVER_PROPERTY),
                    resource.getString(USERNAME_PROPERTY),
                    resource.getString(PASSWORD_PROPERTY),
                    resource.getString(URL_PROPERTY)
            );
            this.validation.validate(properties);
            return properties;
        } catch (MissingResourceException exception) {
            LOGGER.error("Missing resource parameter: {}", exception.getMessage());
        }
        return properties;
    }
}