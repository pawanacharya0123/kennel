package com.kennel.backend.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(Class<?> entityClass, String fieldName, Object fieldValue){
        super(generateMessage(entityClass.getSimpleName(), fieldName, fieldValue));
    }

    private static String generateMessage(String entityName, String fieldName, Object fieldValue) {
        return String.format("%s not found with %s: '%s'", entityName, fieldName, fieldValue);
    }
}
