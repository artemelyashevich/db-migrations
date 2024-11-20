package org.elyashevich.dbmigration.exception;

public class InvalidDatabasePropertiesException extends RuntimeException {
    public InvalidDatabasePropertiesException() {
    }

    public InvalidDatabasePropertiesException(String message) {
        super(message);
    }
}
