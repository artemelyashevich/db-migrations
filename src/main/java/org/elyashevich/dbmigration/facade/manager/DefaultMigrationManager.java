package org.elyashevich.dbmigration.facade.manager;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.elyashevich.dbmigration.service.MigrationExecutorService;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.service.MigrationHistoryService;


public class DefaultMigrationManager implements MigrationManager {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    private final MigrationExecutorService migrationExecutor;
    private final MigrationHistoryService migrationHistoryService;

    public DefaultMigrationManager(MigrationExecutorService executor, MigrationHistoryService migrationHistoryService) {
        this.migrationExecutor = executor;
        this.migrationHistoryService = migrationHistoryService;
    }

    @Override
    public void migrate(final List<MigrationFile> migrationFiles, final Connection connection) {
        int latestVersion = this.migrationHistoryService.findCurrentVersion(connection);

        for (var migration : migrationFiles) {
            if (latestVersion >= migration.getVersion()) {
                continue;
            }

            try {
                this.migrationExecutor.apply(migration.getContent(), connection);
                this.migrationHistoryService.saveMigration(migration, connection);
                latestVersion = migration.getVersion();

                LOGGER.info("Applied migration: {}", migration.getFilename());
            } catch (SQLException e) {
                LOGGER.error("Failed to apply migration: {}", e.getMessage());
                return;
            }
        }
    }
}
