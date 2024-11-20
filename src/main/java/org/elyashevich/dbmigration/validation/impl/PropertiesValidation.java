package org.elyashevich.dbmigration.validation.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.domain.DatabaseProperties;
import org.elyashevich.dbmigration.exception.InvalidDatabasePropertiesException;
import org.elyashevich.dbmigration.validation.Validation;

public class PropertiesValidation implements Validation<DatabaseProperties> {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    @Override
    public void validate(final DatabaseProperties properties) {
        validateProperty(properties.username(), "Username");
        validateProperty(properties.url(), "URL");
        validateProperty(properties.password(), "Password");
        validateProperty(properties.driver(), "Driver");
    }

    private static void validateProperty(final String property, final String propertyName) {
        if (property == null || property.trim().isEmpty()) {
            var message = propertyName + " must not be null or empty.";
            LOGGER.warn(message);
            throw new InvalidDatabasePropertiesException(message);
        }
    }
}
