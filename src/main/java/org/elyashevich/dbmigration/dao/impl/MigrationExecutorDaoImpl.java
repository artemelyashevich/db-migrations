package org.elyashevich.dbmigration.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.dao.MigrationExecutorDao;
import org.elyashevich.dbmigration.exception.MigrationFilesException;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationExecutorDaoImpl implements MigrationExecutorDao {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    @Override
    public void apply(final String query, final Connection connection) throws SQLException {
        try (var prepareStatement = connection.prepareStatement(query)) {
            LOGGER.info("Attempting to apply actual migrations");
            connection.setAutoCommit(false);
            prepareStatement.execute();
            connection.commit();
            LOGGER.info("Actual migrations have been applied");
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Failed to apply migration: {}", e.getMessage());
            throw new MigrationFilesException("Failed to apply migration: %s".formatted(query));
        }
    }
}
