package org.elyashevich.dbmigration.facade.manager;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.elyashevich.dbmigration.exception.MigrationFilesException;
import org.elyashevich.dbmigration.service.MigrationExecutorService;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.service.MigrationHistoryService;
import org.elyashevich.dbmigration.util.MigrationUtil;


public class DefaultMigrationManager implements MigrationManager {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();
    private static final int DELAY_TO_RETRIEVE_LOCKED_STATUS = 5000;

    private final MigrationExecutorService migrationExecutor;
    private final MigrationHistoryService migrationHistoryService;

    public DefaultMigrationManager(MigrationExecutorService executor, MigrationHistoryService migrationHistoryService) {
        this.migrationExecutor = executor;
        this.migrationHistoryService = migrationHistoryService;
    }

    @Override
    public void migrate(final List<MigrationFile> migrationFiles, final Connection connection) {
        var isLocked = this.migrationHistoryService.isLocked(connection);
        while (isLocked) {
            try {
                TimeUnit.MILLISECONDS.sleep(DELAY_TO_RETRIEVE_LOCKED_STATUS);

                LOGGER.info("Migration is locked...");

                isLocked = this.migrationHistoryService.isLocked(connection);
            } catch (InterruptedException e) {
                LOGGER.error("Something went wrong");
            }
        }
        this.processMigration(migrationFiles, connection);
    }

    private void processMigration(final List<MigrationFile> migrationFiles, final Connection connection) {
        var latestVersion = this.migrationHistoryService.findCurrentVersion(connection);

        var migrations = MigrationUtil.filterByVersion(migrationFiles, latestVersion);
        var migrationsScript = MigrationUtil.extractMigrationScripts(migrations);

        try {
            this.migrationExecutor.apply(migrationsScript, connection);
            this.migrationHistoryService.saveMigrations(migrations, connection);
        } catch (SQLException | MigrationFilesException e) {
            LOGGER.error("Failed to apply migration: {}", e.getMessage());
        }
    }
}
