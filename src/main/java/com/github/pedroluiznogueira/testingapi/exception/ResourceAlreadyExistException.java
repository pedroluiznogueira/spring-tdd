package com.github.pedroluiznogueira.testingapi.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ResourceAlreadyExistException extends RuntimeException {
    String resourceName;
    String fieldName;
    String fieldValue;

    public ResourceAlreadyExistException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s with %s : '%s' already exists", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
