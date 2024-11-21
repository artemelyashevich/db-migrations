package org.elyashevich.dbmigration.util;

import org.elyashevich.dbmigration.domain.DatabaseProperties;
import org.elyashevich.dbmigration.exception.InvalidDatabasePropertiesException;
import org.elyashevich.dbmigration.validation.Validation;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertiesUtil {

    private static final String DRIVER_PROPERTY = "migrations.db.driver";
    private static final String USERNAME_PROPERTY = "migrations.db.username";
    private static final String PASSWORD_PROPERTY = "migrations.db.password";
    private static final String URL_PROPERTY = "migrations.db.url";
    private static final String ERROR_TEMPLATE = "Unable to load '%s.properties' file";

    private final Validation<DatabaseProperties> validation;

    public PropertiesUtil(Validation<DatabaseProperties> validation) {
        this.validation = validation;
    }

    public DatabaseProperties loadProperties(final String filename) {
        try {
            var resource = ResourceBundle.getBundle(filename);
            var properties = new DatabaseProperties(
                    resource.getString(DRIVER_PROPERTY),
                    resource.getString(USERNAME_PROPERTY),
                    resource.getString(PASSWORD_PROPERTY),
                    resource.getString(URL_PROPERTY)
            );
            this.validation.validate(properties);
            return properties;
        } catch (MissingResourceException | InvalidDatabasePropertiesException e) {
            throw new IllegalStateException(ERROR_TEMPLATE.formatted(filename), e);
        }
    }
}