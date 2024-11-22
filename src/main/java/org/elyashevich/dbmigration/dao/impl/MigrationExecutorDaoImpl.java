package org.elyashevich.dbmigration.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.dao.MigrationExecutorDao;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationExecutorDaoImpl implements MigrationExecutorDao {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    @Override
    public void apply(final String query, final Connection connection) throws SQLException {
        try (var prepareStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            prepareStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            LOGGER.error("Failed to apply migration: {}", e.getMessage());
        }
    }
}
