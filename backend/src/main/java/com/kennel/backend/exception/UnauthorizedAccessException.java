package com.kennel.backend.exception;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(Class<?> entityClass,  Long entityId){
        super(generateMessage(entityClass.getSimpleName(), entityId));
    }

    private static String generateMessage(String entityName, Long id) {
        return String.format("You are not authorized to access %s with ID %d.", entityName, id);
    }
}
