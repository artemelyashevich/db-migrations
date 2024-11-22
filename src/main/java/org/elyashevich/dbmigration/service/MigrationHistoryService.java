package org.elyashevich.dbmigration.service;

import org.elyashevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;
import java.util.List;

public interface MigrationHistoryService {

    void saveMigrations(final List<MigrationFile> migrations, final Connection connection);

    Integer findCurrentVersion(final Connection connection);

    Boolean isLocked(final Connection connection);
}
