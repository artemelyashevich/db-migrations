package org.elyashevich.dbmigration.validation.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.exception.InvalidDatabasePropertiesException;
import org.elyashevich.dbmigration.exception.MigrationFilesException;
import org.elyashevich.dbmigration.validation.Validation;

public class MigrationFileValidation implements Validation<MigrationFile> {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    @Override
    public void validate(final MigrationFile migrationFile) {
        validateProperty(migrationFile.getFilename(), "Migration name");
        validateProperty(migrationFile.getContent(), "Migration content");
        validateProperty(migrationFile.getVersion().toString(), "Migration version");
    }

    // TODO
    private static void validateProperty(final String property, final String propertyName) {
        if (property.isBlank()) {
            var message = propertyName + " must not be null or empty.";
            LOGGER.warn(message);
            throw new MigrationFilesException(message);
        }
    }
}
