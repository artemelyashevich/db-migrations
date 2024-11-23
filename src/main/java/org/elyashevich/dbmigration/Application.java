package org.elyashevich.dbmigration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.facade.MigrationFacade;

import java.util.Locale;
import java.util.Scanner;

public class Application {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    public static void main(String[] args) {
        var migrationFacade = new MigrationFacade();

        LOGGER.info("Enter a command ('migrate', 'status'):");

        var command = new Scanner(System.in).nextLine();

        if (command.isBlank()) {
            LOGGER.info("Available commands: 'migrate', 'status'.");
        } else {
            switch (command.toLowerCase()) {
                case "migrate":
                    migrationFacade.migrate();
                    break;
                case "status":
                    migrationFacade.printAppliedMigrations();
                    break;
                default:
                    LOGGER.info("Invalid command. Available commands: migrate, status");
                    break;
            }
        }
    }
}
