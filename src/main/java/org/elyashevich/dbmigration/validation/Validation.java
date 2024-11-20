package org.elyashevich.dbmigration.validation;

public interface Validation<T> {

    void validate(final T value);
}
