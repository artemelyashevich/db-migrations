package org.elyashevich.dbmigration.facade.reader;

import org.elyashevich.dbmigration.domain.DatabaseProperties;
import org.elyashevich.dbmigration.exception.BrokenValidationException;
import org.elyashevich.dbmigration.facade.reader.validator.Validator;
import org.elyashevich.dbmigration.facade.reader.validator.impl.PropertiesValidator;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.elyashevich.dbmigration.util.MigrationConstant.DRIVER_PROPERTY;
import static org.elyashevich.dbmigration.util.MigrationConstant.PASSWORD_PROPERTY;
import static org.elyashevich.dbmigration.util.MigrationConstant.URL_PROPERTY;
import static org.elyashevich.dbmigration.util.MigrationConstant.USERNAME_PROPERTY;

public class MigrationPropertyReader {

    private static final String ERROR_TEMPLATE = "Unable to load '%s.properties' file";

    private static final Validator<DatabaseProperties> validator = new PropertiesValidator();

    public static DatabaseProperties loadProperties(final String filename) {
        try {
            var resource = ResourceBundle.getBundle(filename);
            var properties = new DatabaseProperties(
                    resource.getString(DRIVER_PROPERTY),
                    resource.getString(USERNAME_PROPERTY),
                    resource.getString(PASSWORD_PROPERTY),
                    resource.getString(URL_PROPERTY)
            );
            validator.validate(properties);
            return properties;
        } catch (MissingResourceException | BrokenValidationException e) {
            throw new IllegalStateException(ERROR_TEMPLATE.formatted(filename), e);
        }
    }
}