package org.elyashevich.dbmigration;

public class Application {

    public static void main(String[] args) {
        var migrationFacade = new MigrationFacade();
        migrationFacade.migrate();
    }
}
