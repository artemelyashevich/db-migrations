package org.elyashevich.dbmigration;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.LogManager;


public class MigrationManager {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();
    private final static String SELECT_CURRENT_VERSION_QUERY = "SELECT version FROM migration_history ORDER BY version DESC LIMIT 1";

    private final MigrationExecutor migrationExecutor;

    public MigrationManager(MigrationExecutor migrationExecutor) {
        this.migrationExecutor = migrationExecutor;
    }

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
                this.migrationExecutor.applyMigration(migration.getContent(), connection);
                this.migrationExecutor.saveMigration(migration.getVersion(), connection);
                LOGGER.info("Applied migration: {}", migration.getFilename());
            }
        });
    }
}
