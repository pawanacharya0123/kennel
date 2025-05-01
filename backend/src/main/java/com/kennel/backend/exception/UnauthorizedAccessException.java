package com.kennel.backend.exception;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(Class<?> entityClass,  Object entityIdentifier){
        super(generateMessage(entityClass.getSimpleName(),  entityIdentifier));
    }

    private static String generateMessage(String entityName, Object entityIdentifier) {
        return String.format("You are not authorized to access %s with ID %d.", entityName, entityIdentifier);
    }
}
