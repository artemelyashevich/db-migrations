package org.elyashevich.dbmigration.facade.reader.validator;

import org.elyashevich.dbmigration.exception.BrokenValidationException;

import java.nio.file.Files;
import java.nio.file.Path;


@FunctionalInterface
public interface Validator<T> {

    void validate(final T value);

    default void validateProperty(final String key, final String value) {
        if (value.isBlank()) {
            throw new BrokenValidationException("'%s' must not be empty.".formatted(key));
        }
    }

    default void validatePath(final Path folder) {
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new BrokenValidationException("No such directory '%s'.".formatted(folder.getFileName().toString()));
        }
    }
}