package org.elyashevich.dbmigration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationExecutor {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();
    private final static String SAVE_MIGRATION_VERSION_QUERY = "INSERT INTO migration_history (version) VALUES (?)";

    public void applyMigration(final String query, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.execute();
        } catch (SQLException exception) {
            LOGGER.warn("Failed to apply migration: {}", exception.getMessage());
            exception.printStackTrace();
        }
    }

    public void saveMigration(final Integer version, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(SAVE_MIGRATION_VERSION_QUERY)) {
            prepareStatement.setInt(1, version);
            prepareStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.warn("Failed to save migration: {}", exception.getMessage());
            exception.printStackTrace();
        }
    }
}
