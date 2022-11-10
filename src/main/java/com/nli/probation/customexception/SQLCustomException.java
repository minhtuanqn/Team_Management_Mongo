package com.nli.probation.customexception;

/**
 * Custom exception about sql(entity,...)
 */
public class SQLCustomException extends RuntimeException{

    public SQLCustomException() {
    }

    public SQLCustomException(String message) {
        super(message);
    }

    public SQLCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLCustomException(Throwable cause) {
        super(cause);
    }

    public SQLCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
