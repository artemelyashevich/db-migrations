package org.elyashevich.dbmigration.service.impl;

import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.dao.MigrationHistoryDao;
import org.elyashevich.dbmigration.service.MigrationHistoryService;

import java.sql.Connection;

public class DefaultMigrationHistoryService implements MigrationHistoryService {

    private final MigrationHistoryDao migrationHistoryDao;

    public DefaultMigrationHistoryService(MigrationHistoryDao migrationHistoryDao) {
        this.migrationHistoryDao = migrationHistoryDao;
    }

    @Override
    public void saveMigration(final MigrationFile migrationFile, final Connection connection) {
        this.migrationHistoryDao.saveMigration(migrationFile, connection);
    }

    @Override
    public Integer findCurrentVersion(final Connection connection) {
        return this.migrationHistoryDao.findCurrentVersion(connection);
    }
}
