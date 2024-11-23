package org.elyashevich.dbmigration.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface MigrationExecutorDao {

    void apply(final List<String> query, final Connection connection) throws SQLException;
}
