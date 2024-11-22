package org.elyashevich.dbmigration.dao;

import org.elyashevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;

public interface MigrationHistoryDao {

    void saveMigration(final MigrationFile migrationFile, final Connection connection);

    Integer findCurrentVersion(final Connection connection);

    Boolean checkIfLocked(final Connection connection);
}
