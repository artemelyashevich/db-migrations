package org.elyashevich.dbmigration.validation.impl;

import org.elyashevich.dbmigration.domain.DatabaseProperties;
import org.elyashevich.dbmigration.exception.InvalidDatabasePropertiesException;
import org.elyashevich.dbmigration.validation.Validation;

public class PropertiesValidation implements Validation<DatabaseProperties> {

    private static final String ERROR_TEMPLATE = "'%s' must not be empty.";

    @Override
    public void validate(final DatabaseProperties properties) {
        validateProperty(properties.username(), "Username");
        validateProperty(properties.url(), "URL");
        validateProperty(properties.password(), "Password");
        validateProperty(properties.driver(), "Driver");
    }

    private static void validateProperty(final String property, final String propertyName) {
        if (property.isBlank()) {
            throw new InvalidDatabasePropertiesException(ERROR_TEMPLATE.formatted(propertyName));
        }
    }
}
