package org.elyashevich.dbmigration;

import org.elyashevich.dbmigration.reader.MigrationFileReader;

public class Application {

    private static final String BASIC_PREFIX = "migrations.db.";
    private static final String PATH_TO_RESOURCES = "src/main/resources/application.properties";

    public static void main(String[] args) {
        var migrationReader = new MigrationFileReader();
        var migrations = migrationReader.readMigrationFiles("src/main/resources/migrations");
    }
}
