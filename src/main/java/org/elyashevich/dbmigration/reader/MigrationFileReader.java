package org.elyashevich.dbmigration.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.exception.MigrationFilesException;
import org.elyashevich.dbmigration.util.FileParserUtil;
import org.elyashevich.dbmigration.validation.Validation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MigrationFileReader {

    private static final Logger LOGGER = LogManager.getLogger();
    private final Validation<MigrationFile> migrationFileValidation;

    public MigrationFileReader(Validation<MigrationFile> migrationFileValidation) {
        this.migrationFileValidation = migrationFileValidation;
    }

    public List<MigrationFile> read(final String pathToMigrations) {
        Objects.requireNonNull(pathToMigrations, "The path to migrations cannot be null");

        var folder = Paths.get(pathToMigrations);

        // TODO: create validator
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new MigrationFilesException("Invalid path: No such directory.");
        }

        try (var paths = Files.list(folder)) {
            return paths.filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".sql"))
                    .map(file -> {
                        var migration = this.parseMigration(file);
                        this.migrationFileValidation.validate(migration);
                        return migration;
                    })
                    .sorted(Comparator.comparing(MigrationFile::getVersion))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Error during reading migration files", e);
        }
    }

    private MigrationFile parseMigration(final Path path) {
        var migrationFile = new MigrationFile();
        try {
            var name = path.getFileName().toString();

            var content = Files.readString(path);
            var version = FileParserUtil.getVersionFromFileName(name);
            var fileName = FileParserUtil.getMigrationFileName(name);

            migrationFile.setFilename(fileName);
            migrationFile.setContent(content);
            migrationFile.setVersion(version);
        } catch (IOException e) {
            LOGGER.error("Error during reading files: {}", e.getMessage());
        }
        return migrationFile;
    }
}
