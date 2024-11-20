package org.elyashevich.dbmigration.reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.elyashevich.dbmigration.domain.MigrationFile;
import org.elyashevich.dbmigration.validation.Validation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class MigrationFileReader {

    private final static Logger LOGGER = (Logger) LogManager.getLogger();
    private final Validation<MigrationFile> migrationFileValidation;

    public MigrationFileReader(Validation<MigrationFile> migrationFileValidation) {
        this.migrationFileValidation = migrationFileValidation;
    }

    public List<MigrationFile> readMigrationFiles(final String pathToMigrations) {
        var folder = new File(pathToMigrations);
        var listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            throw new RuntimeException("Where no files.");
        }
        return Arrays.stream(listOfFiles)
                .filter(file -> getFileExtension(file.getName()).equals(".sql"))
                .map(file -> {
                    var migration = this.readMigration(file);
                    this.migrationFileValidation.validate(migration);
                    return migration;
                })
                .toList();
    }

    private MigrationFile readMigration(final File file) {
        var migrationFile = new MigrationFile();
        try {
            var name = file.getName();
            var content = Files.readString(file.toPath());
            var version = getVersionFromFileName(name);
            var fileName = getMigrationFileName(name);
            migrationFile.setFilename(fileName);
            migrationFile.setContent(content);
            migrationFile.setVersion(version);
        } catch (IOException e) {
            LOGGER.warn("Error during reading files.");
            e.printStackTrace();
        }
        return migrationFile;
    }

    private String getFileExtension(String fileName) {
        return (fileName.lastIndexOf('.') == -1) ? "" : fileName.substring(fileName.lastIndexOf('.'));
    }

    private int getVersionFromFileName(String fileName) {
        var pattern = Pattern.compile("V_(\\d+)");
        var matcher = pattern.matcher(fileName);
        int version = 0;
        if (matcher.find()) {
            version = Integer.parseInt(matcher.group(1));
        }
        return version;
    }

    private String getMigrationFileName(String fileName) {
        String[] fileNameParts = fileName.split("\\.");
        return (fileNameParts.length > 0) ? fileNameParts[0] : fileName;
    }
}
