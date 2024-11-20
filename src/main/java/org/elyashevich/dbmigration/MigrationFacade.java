package org.elyashevich.dbmigration;

import org.elyashevich.dbmigration.config.ConnectionManager;
import org.elyashevich.dbmigration.config.PropertiesUtil;
import org.elyashevich.dbmigration.core.executor.impl.MigrationExecutor;
import org.elyashevich.dbmigration.core.manager.impl.MigrationManagerImpl;
import org.elyashevich.dbmigration.history.dao.impl.MigrationHistoryDaoImpl;
import org.elyashevich.dbmigration.history.service.impl.MigrationHistoryServiceImpl;
import org.elyashevich.dbmigration.reader.MigrationFileReader;
import org.elyashevich.dbmigration.validation.impl.MigrationFileValidation;
import org.elyashevich.dbmigration.validation.impl.PropertiesValidation;

public class MigrationFacade {

    private static final String PATH_TO_RESOURCES = "application";
    private static final String PATH_TO_MIGRATIONS = "src/main/resources/migrations";

    private final MigrationFileReader migrationFileReader;
    private final MigrationManagerImpl migrationManager;
    private final ConnectionManager connectionManager;
    private final PropertiesUtil propertiesUtil;

    public MigrationFacade() {
        this.migrationFileReader = new MigrationFileReader(new MigrationFileValidation());
        var historyService = new MigrationHistoryServiceImpl(new MigrationHistoryDaoImpl());
        var executor = new MigrationExecutor();
        this.migrationManager = new MigrationManagerImpl(executor, historyService);
        this.connectionManager = ConnectionManager.getInstance();
        this.propertiesUtil = new PropertiesUtil(new PropertiesValidation());
    }

    public void migrate() {
        var migrations = this.migrationFileReader.readMigrationFiles(PATH_TO_MIGRATIONS);
        var properties = this.propertiesUtil.loadProperties(PATH_TO_RESOURCES);
        var connection = this.connectionManager.getConnection(properties);
        this.migrationManager.migrate(migrations, connection);
    }
}
