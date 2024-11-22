package org.elyashevich.dbmigration.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.dao.MigrationHistoryDao;
import org.elyashevich.dbmigration.domain.MigrationFile;

import java.sql.Connection;
import java.sql.SQLException;

public class MigrationHistoryDaoImpl implements MigrationHistoryDao {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    private static final String SELECT_CURRENT_VERSION_QUERY =
            "SELECT version FROM migration_history ORDER BY version DESC LIMIT 1";
    private static final String SELECT_UNLOCKED_MIGRATIONS = "SELECT * FROM migration_history WHERE is_locked = 1";
    private static final String SAVE_MIGRATION_QUERY =
            "INSERT INTO migration_history (name, version, is_locked) VALUES (?, ?, ?);";
    private static final String CREATE_MIGRATION_HISTORY_TABLE_QUERY = """
            CREATE TABLE IF NOT EXISTS migration_history (
                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(255) NOT NULL,
                version INT NOT NULL UNIQUE,
                is_locked TINYINT(1) NOT NULL,
                applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
            """;
    private static final String UPDATE_TO_UNLOCKED_STATUS_QUERY = "UPDATE migration_history SET is_locked = 0 WHERE version = ?;";

    @Override
    public void saveMigration(final MigrationFile migrationFile, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(SAVE_MIGRATION_QUERY)) {
            LOGGER.info("Attempting to save migration {}", migrationFile.getFilename());

            prepareStatement.setString(1, migrationFile.getFilename());
            prepareStatement.setInt(2, migrationFile.getVersion());
            prepareStatement.setInt(3, 1);
            prepareStatement.executeUpdate();
            unlock(migrationFile.getVersion(), connection);

            LOGGER.info("Migration has been saved {}", migrationFile.getFilename());
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

    @Override
    public Boolean checkIfLocked(Connection connection) {
        try (var prepareStatement = connection.prepareStatement(SELECT_UNLOCKED_MIGRATIONS);
             var resultSet = prepareStatement.executeQuery()) {
            return resultSet.next();
        } catch (SQLException exception) {
            LOGGER.error("Error to find locked migrations: {}", exception.getMessage());
        }
        return false;
    }

    private static void createTable(final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(CREATE_MIGRATION_HISTORY_TABLE_QUERY)) {
            prepareStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Error creating migration_history table: {}", exception.getMessage());
        }
    }

    private static void unlock(final Integer version, final Connection connection) {
        try (var prepareStatement = connection.prepareStatement(UPDATE_TO_UNLOCKED_STATUS_QUERY)) {
            prepareStatement.setInt(1, version);
            prepareStatement.executeUpdate();
        } catch (SQLException exception) {
            LOGGER.error("Error unlocking migration: {}", exception.getMessage());
        }
    }
}
