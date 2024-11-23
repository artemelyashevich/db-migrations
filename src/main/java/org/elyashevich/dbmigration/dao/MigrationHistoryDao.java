package org.elyashevich.dbmigration.dao;

import org.elyashevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;
import java.util.List;

public interface MigrationHistoryDao {

    void saveMigration(final MigrationFile migrationFile, final Connection connection);

    Integer findCurrentVersion(final Connection connection);

    Boolean checkIfLocked(final Connection connection);

    void unlock(final Integer version, final Connection connection);

    List<MigrationFile> findInfo(final Connection connection);
}
