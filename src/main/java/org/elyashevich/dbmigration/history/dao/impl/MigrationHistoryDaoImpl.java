package org.elyashevich.dbmigration.history.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.history.dao.MigrationHistoryDao;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationHistoryDaoImpl implements MigrationHistoryDao {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();

    private final static String SELECT_CURRENT_VERSION_QUERY =
            "SELECT version FROM migration_history ORDER BY version DESC LIMIT 1";
    private final static String SAVE_MIGRATION_VERSION_QUERY =
            "INSERT INTO migration_history (version, is_locked) VALUES (?, ?);";
    private final static String CREATE_MIGRATION_HISTORY_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS migration_history (" +
            "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
            "version INT NOT NULL UNIQUE, " +
            "is_locked TINYINT(1) NOT NULL, " +
            "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ");";

    @Override
    public void saveMigration(final MigrationFile migrationFile, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(SAVE_MIGRATION_VERSION_QUERY)) {
            prepareStatement.setInt(1, migrationFile.getVersion());
            prepareStatement.setInt(2, 0);
            prepareStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Failed to save migration: {}", exception.getMessage());
        }
    }

    @Override
    public Integer findCurrentVersion(final Connection connection) {
        createTable(connection);
        try (var prepareStatement = connection.prepareStatement(SELECT_CURRENT_VERSION_QUERY);
             var resultSet = prepareStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("version");
            }
        } catch (SQLException exception) {
            LOGGER.error("Error getting current version: {}", exception.getMessage());
        }
        return 0;
    }

    private static void createTable(final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(CREATE_MIGRATION_HISTORY_TABLE_QUERY)) {
            prepareStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Error creating migration_history table: {}", exception.getMessage());
        }
    }
}
