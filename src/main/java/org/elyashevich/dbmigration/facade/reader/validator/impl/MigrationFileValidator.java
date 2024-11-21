package org.elyashevich.dbmigration.facade.reader.validator.impl;

import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.facade.reader.validator.Validator;

public class MigrationFileValidator implements Validator<MigrationFile> {

    @Override
    public void validate(final MigrationFile migrationFile) {
        validateProperty("Migration name", migrationFile.getFilename());
        validateProperty("Migration content", migrationFile.getContent());
        validateProperty("Migration version", migrationFile.getVersion().toString());
    }
}