package org.elyashevich.dbmigration.history.service;

import org.elyashevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;

public interface MigrationHistoryService {

    void saveMigration(final MigrationFile migrationFile, final Connection connection);

    Integer findCurrentVersion(final Connection connection);
}
