package org.elyashevich.dbmigration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationHistoryManager {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();

    private final static String CREATE_MIGRATION_HISTORY_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS migration_history (" +
            "id SERIAL PRIMARY KEY, " +
            "version INT NOT NULL UNIQUE, " +
            "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ");";

    public void initialize(final Connection connection) {
        this.createMigrationHistoryTable(connection);
    }

    private void createMigrationHistoryTable(final Connection connection) {
        try (var statement = connection.createStatement()) {
            statement.execute(CREATE_MIGRATION_HISTORY_TABLE_QUERY);
            LOGGER.info("Table migration_history created successfully.");
        } catch (SQLException exception) {
            LOGGER.warn("Error creating table migration_history: {}", exception.getMessage() );
            exception.printStackTrace();
        }
    }
}
