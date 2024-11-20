package org.elyashevich.dbmigration.core.executor;

import java.sql.Connection;

public interface Executor {

    void applyMigration(final String content, final Connection connection);
}
