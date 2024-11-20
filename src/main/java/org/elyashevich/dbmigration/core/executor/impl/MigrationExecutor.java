package org.elyashevich.dbmigration.core.executor.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.core.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationExecutor implements Executor {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();

    public void applyMigration(final String query, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(query)) {
            prepareStatement.execute();
        } catch (SQLException exception) {
            LOGGER.warn("Failed to apply migration: {}", exception.getMessage());
            exception.printStackTrace();
        }
    }
}
