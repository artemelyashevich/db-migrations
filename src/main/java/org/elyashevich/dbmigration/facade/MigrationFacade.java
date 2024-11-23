package org.elyashevich.dbmigration.facade;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.facade.db.ConnectionManager;
import org.elyashevich.dbmigration.service.impl.DefaultMigrationExecutor;
import org.elyashevich.dbmigration.exception.BrokenValidationException;
import org.elyashevich.dbmigration.facade.manager.DefaultMigrationManager;
import org.elyashevich.dbmigration.facade.reader.MigrationPropertyReader;
import org.elyashevich.dbmigration.domain.DatabaseProperties;
import org.elyashevich.dbmigration.dao.impl.MigrationHistoryDaoImpl;
import org.elyashevich.dbmigration.service.impl.DefaultMigrationHistoryService;
import org.elyashevich.dbmigration.facade.reader.MigrationFileReader;

import java.sql.SQLException;

import static org.elyashevich.dbmigration.util.ConfigurationConstant.*;


public class MigrationFacade {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    private static final DatabaseProperties properties;

    private final MigrationFileReader fileReader;
    private final DefaultMigrationManager migrationManager;
    private final ConnectionManager connectionManager;

    static {
        properties = MigrationPropertyReader.loadProperties(PATH_TO_RESOURCES);
    }

    public MigrationFacade() {
        this.fileReader = new MigrationFileReader();
        this.connectionManager = ConnectionManager.getInstance();

        var historyService = new DefaultMigrationHistoryService(new MigrationHistoryDaoImpl());
        var executor = new DefaultMigrationExecutor();
        this.migrationManager = new DefaultMigrationManager(executor, historyService);
    }

    public void migrate() {
        try {
            var migrations = this.fileReader.read(PATH_TO_MIGRATIONS);

            try (var connection = this.connectionManager.getConnection(properties)) {
                this.migrationManager.migrate(migrations, connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (BrokenValidationException e) {
            LOGGER.error("Failed to validate migrations files: {}", e.getMessage());
        } catch (RuntimeException e) {
            LOGGER.error("Unable to parse migrations files: {}", e.getMessage());
        }
    }
}
