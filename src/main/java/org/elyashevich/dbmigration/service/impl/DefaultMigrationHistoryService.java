package org.elyashevich.dbmigration.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.dao.MigrationHistoryDao;
import org.elyashevich.dbmigration.service.MigrationHistoryService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DefaultMigrationHistoryService implements MigrationHistoryService {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    private final MigrationHistoryDao migrationHistoryDao;

    public DefaultMigrationHistoryService(MigrationHistoryDao migrationHistoryDao) {
        this.migrationHistoryDao = migrationHistoryDao;
    }

    @Override
    public void saveMigrations(final List<MigrationFile> migrations, final Connection connection) {
        migrations.forEach(migration -> {
            this.migrationHistoryDao.saveMigration(migration, connection);

            LOGGER.info("Applied migration: {}", migration.getFilename());
        });
    }

    @Override
    public Integer findCurrentVersion(final Connection connection) {
        return this.migrationHistoryDao.findCurrentVersion(connection);
    }

    @Override
    public Boolean isLocked(final Connection connection) {
        return this.migrationHistoryDao.checkIfLocked(connection);
    }
}
