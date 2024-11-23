package org.elyashevich.dbmigration.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@FunctionalInterface
public interface MigrationExecutorService {

    void apply(final List<String> content, final Connection connection) throws SQLException;
}
