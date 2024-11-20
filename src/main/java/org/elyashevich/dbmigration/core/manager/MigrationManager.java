package org.elyashevich.dbmigration.core.manager;

import org.elyashevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;
import java.util.List;

public interface MigrationManager {

    void migrate(final List<MigrationFile> migrationFiles, final Connection connection);
}