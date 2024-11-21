package org.elyashevich.dbmigration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.core.db.ConnectionManager;
import org.elyashevich.dbmigration.util.PropertiesUtil;
import org.elyashevich.dbmigration.core.executor.impl.MigrationExecutor;
import org.elyashevich.dbmigration.core.manager.impl.MigrationManagerImpl;
import org.elyashevich.dbmigration.domain.DatabaseProperties;
import org.elyashevich.dbmigration.exception.MigrationFilesException;
import org.elyashevich.dbmigration.history.dao.impl.MigrationHistoryDaoImpl;
import org.elyashevich.dbmigration.history.service.impl.MigrationHistoryServiceImpl;
import org.elyashevich.dbmigration.reader.MigrationFileReader;
import org.elyashevich.dbmigration.validation.impl.MigrationFileValidation;
import org.elyashevich.dbmigration.validation.impl.PropertiesValidation;


public class MigrationFacade {

    private static final Logger LOGGER = (Logger) LogManager.getLogger();

    private static final String PATH_TO_RESOURCES = "application";
    private static final String PATH_TO_MIGRATIONS = "src/main/resources/migrations";

    private static final DatabaseProperties properties;

    private final MigrationFileReader migrationFileReader;
    private final MigrationManagerImpl migrationManager;
    private final ConnectionManager connectionManager;

    static {
        var propertiesUtil = new PropertiesUtil(new PropertiesValidation());
        properties = propertiesUtil.loadProperties(PATH_TO_RESOURCES);
    }

    public MigrationFacade() {
        this.migrationFileReader = new MigrationFileReader(new MigrationFileValidation());
        this.connectionManager = ConnectionManager.getInstance();

        var historyService = new MigrationHistoryServiceImpl(new MigrationHistoryDaoImpl());
        var executor = new MigrationExecutor();
        this.migrationManager = new MigrationManagerImpl(executor, historyService);
    }

    public void migrate() {
        try {
            var migrations = this.migrationFileReader.read(PATH_TO_MIGRATIONS);
            var connection = this.connectionManager.getConnection(properties);

            this.migrationManager.migrate(migrations, connection);
        } catch (IllegalStateException | MigrationFilesException e) {
            LOGGER.error("Failed to parse migrations files: {}", e.getMessage());
        }
    }
}
