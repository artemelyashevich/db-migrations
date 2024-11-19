package org.elyashevich.dbmigration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MigrationFileReader {

    public List<MigrationFile> readMigrationFiles(final String pathToMigrations) {
        var migrations = new ArrayList<MigrationFile>();
        var folder = new File(pathToMigrations);
        var listOfFiles = folder.listFiles();
        if (listOfFiles == null) {
            throw new RuntimeException("Where no files.");
        }
        Arrays.stream(listOfFiles).forEach(file -> {
                    if (file.isFile()) {
                      migrations.add(this.readMigration(file));
                    }
                }
        );
        return migrations;
    }

    private MigrationFile readMigration(final File file) {
        var migrationFile = new MigrationFile();
        try {
            var name = file.getName();
            int dotIndex = name.lastIndexOf('.');
            var extension = (dotIndex == -1) ? "" : name.substring(dotIndex);
            if (!extension.equals(".sql")) {
                throw new RuntimeException("No supported extension.");
            }
            var content = Files.readString(file.toPath());
            migrationFile.setFilename(name.split("\\.")[1]);
            migrationFile.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return migrationFile;
    }
}
