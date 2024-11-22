package org.elyashevich.dbmigration.util;

import org.elyashevich.dbmigration.domain.MigrationFile;

import java.util.List;

public final class MigrationUtil {

    private MigrationUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static List<MigrationFile> filterByVersion(final List<MigrationFile> migrations, final Integer version) {
        return migrations.stream()
                .filter(migration -> migration.getVersion() > version)
                .toList();
    }

    public static String extractMigrationScripts(final List<MigrationFile> migrationFiles) {
        return migrationFiles.stream()
                .map(migration -> "\n" + migration.getContent())
                .collect(StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append)
                .toString();
    }
}
