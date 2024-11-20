package org.elyashevich.dbmigration.core.manager.impl;

import org.apache.logging.log4j.core.Logger;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.elyashevich.dbmigration.core.executor.Executor;
import org.elyashevich.dbmigration.core.manager.MigrationManager;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.history.service.MigrationHistoryService;


public class MigrationManagerImpl implements MigrationManager {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();

    private final Executor migrationExecutor;
    private final MigrationHistoryService migrationHistoryService;

    public MigrationManagerImpl(Executor executor, MigrationHistoryService migrationHistoryService) {
        this.migrationExecutor = executor;
        this.migrationHistoryService = migrationHistoryService;
    }

    @Override
    public void migrate(final List<MigrationFile> migrationFiles, final Connection connection) {
        int latestVersion = this.migrationHistoryService.findCurrentVersion(connection);
        for (MigrationFile migration : migrationFiles) {
            if (latestVersion >= migration.getVersion()) {
                continue;
            }
            this.migrationExecutor.applyMigration(migration.getContent(), connection);
            this.migrationHistoryService.saveMigration(migration, connection);
            LOGGER.info("Applied migration: {}", migration.getFilename());
            latestVersion = migration.getVersion();
        }
    }
}
