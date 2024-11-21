package org.elyashevich.dbmigration.core.executor.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.core.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationExecutor implements Executor {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    public void apply(final String query, final Connection connection) throws SQLException {
        try (var prepareStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            prepareStatement.execute();
            connection.commit();
        } catch (SQLException exception) {
            connection.rollback();
            LOGGER.error("Failed to apply migration: {}", exception.getMessage());
        }
    }
}
