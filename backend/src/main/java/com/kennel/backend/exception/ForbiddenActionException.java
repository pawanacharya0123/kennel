package com.kennel.backend.exception;

public class ForbiddenActionException extends RuntimeException{
    public ForbiddenActionException(Class<?> entityClass){
        super(generateMessage(entityClass.getSimpleName()));
    }

    private static String generateMessage(String entityName) {
        return String.format("Not allowed to modify data on : %s", entityName);
    }
}
