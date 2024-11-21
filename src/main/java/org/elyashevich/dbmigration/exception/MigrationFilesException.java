package org.elyashevich.dbmigration.exception;

public class MigrationFilesException extends RuntimeException{
    public MigrationFilesException() {
    }

    public MigrationFilesException(String message) {
        super(message);
    }
}
