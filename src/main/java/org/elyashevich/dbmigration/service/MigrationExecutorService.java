package org.elyashevich.dbmigration.service;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface MigrationExecutorService {

    void apply(final String content, final Connection connection) throws SQLException;
}
