package org.elyashevich.dbmigration;

import org.elyashevich.dbmigration.facade.MigrationFacade;

public class Application {

    public static void main(String[] args) {
        var migrationFacade = new MigrationFacade();
        migrationFacade.migrate();
    }
}
