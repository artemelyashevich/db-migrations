package org.elyashevich.dbmigration.facade.manager;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.elyashevich.dbmigration.exception.MigrationFilesException;
import org.elyashevich.dbmigration.service.MigrationExecutorService;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.service.MigrationHistoryService;
import org.elyashevich.dbmigration.util.MigrationUtil;


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
        var isLocked = this.migrationHistoryService.isLocked(connection);
        while(isLocked) {
            try {
                Thread.sleep(5000);
                LOGGER.info("Migration is locked...");
                isLocked = this.migrationHistoryService.isLocked(connection);
            } catch (InterruptedException e) {
                LOGGER.error("Something went wrong");
            }
        }
        var latestVersion = this.migrationHistoryService.findCurrentVersion(connection);

        var migrations = MigrationUtil.filterByVersion(migrationFiles, latestVersion);
        var migrationsScript = MigrationUtil.extractMigrationScripts(migrationFiles);

        try {
            this.migrationExecutor.apply(migrationsScript, connection);
            this.migrationHistoryService.saveMigrations(migrations, connection);
        } catch (SQLException | MigrationFilesException e) {
            LOGGER.error("Failed to apply migration: {}", e.getMessage());
        }
    }
}
