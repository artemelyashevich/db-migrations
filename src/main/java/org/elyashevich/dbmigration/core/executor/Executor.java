package org.elyashevich.dbmigration.core.executor;

import java.sql.Connection;
import java.sql.SQLException;

public interface Executor {

    void applyMigration(final String content, final Connection connection) throws SQLException;
}
