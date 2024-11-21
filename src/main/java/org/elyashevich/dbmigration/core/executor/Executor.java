package org.elyashevich.dbmigration.core.executor;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface Executor {

    void apply(final String content, final Connection connection) throws SQLException;
}
