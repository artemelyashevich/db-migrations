package org.elyashevich.dbmigration.dao;

import java.sql.Connection;
import java.sql.SQLException;

public interface MigrationExecutorDao {

    void apply(final String query, final Connection connection) throws SQLException;
}
