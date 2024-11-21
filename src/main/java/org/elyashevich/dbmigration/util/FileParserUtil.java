package org.elyashevich.dbmigration.util;

import java.util.regex.Pattern;

public class FileParserUtil {

    public static String getFileExtension(String fileName) {
        return (fileName.lastIndexOf('.') == -1) ? "" : fileName.substring(fileName.lastIndexOf('.'));
    }

    public static int getVersionFromFileName(String fileName) {
        var pattern = Pattern.compile("V_(\\d+)");
        var matcher = pattern.matcher(fileName);
        int version = 0;
        if (matcher.find()) {
            version = Integer.parseInt(matcher.group(1));
        }
        return version;
    }

    public static String getMigrationFileName(String fileName) {
        String[] fileNameParts = fileName.split("\\.");
        return (fileNameParts.length > 0) ? fileNameParts[0] : fileName;
    }
}
