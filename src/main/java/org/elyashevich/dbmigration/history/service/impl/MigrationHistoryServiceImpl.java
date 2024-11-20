package org.elyashevich.dbmigration.history.service.impl;

import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.history.dao.MigrationHistoryDao;
import org.elyashevich.dbmigration.history.service.MigrationHistoryService;

import java.sql.Connection;

public class MigrationHistoryServiceImpl implements MigrationHistoryService {

    private final MigrationHistoryDao migrationHistoryDao;

    public MigrationHistoryServiceImpl(MigrationHistoryDao migrationHistoryDao) {
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
