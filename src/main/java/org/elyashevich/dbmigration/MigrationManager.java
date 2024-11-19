package org.elyashevich.dbmigration;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;


public class MigrationManager {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();
    private final static String SAVE_MIGRATION_VERSION_QUERY = "INSERT INTO migration_history (version) VALUES (?)";
    private final static String SELECT_CURRENT_VERSION_QUERY = "SELECT version FROM migration_history ORDER BY version DESC LIMIT 1";

    public int getCurrentVersion(final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(SELECT_CURRENT_VERSION_QUERY);
             var resultSet = prepareStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("version");
            }
        } catch (SQLException exception) {
            LOGGER.warn("Error getting current version: {}", exception.getMessage());
            exception.printStackTrace();
        }
        return 0;
    }

    public void migrate(final List<MigrationFile> migrationFiles, final Connection connection) {
        var currentVersion = this.getCurrentVersion(connection);
        migrationFiles.forEach(migration -> {
            if (migration.getVersion() > currentVersion) {
                this.applyMigration(migration.getContent(), connection);
                this.saveMigration(migration.getVersion(), connection);
                LOGGER.info("Applied migration: {}", migration.getFilename());
            }
        });
    }

    private void applyMigration(final String query, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.execute();
        } catch (SQLException exception) {
            LOGGER.warn("Failed to apply migration: {}", exception.getMessage());
            exception.printStackTrace();
        }
    }

    private void saveMigration(final Integer version, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(SAVE_MIGRATION_VERSION_QUERY)) {
            prepareStatement.setInt(1, version);
            prepareStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.warn("Failed to save migration: {}", exception.getMessage());
            exception.printStackTrace();
        }
    }
}
