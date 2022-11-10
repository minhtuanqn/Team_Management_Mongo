package com.nli.probation.customexception;

/**
 * Custom exception for no such name of field in a class
 */
public class NoSuchFieldOfClassException extends ClassCustomException{
    public NoSuchFieldOfClassException(String message) {
        super(message);
    }
}
