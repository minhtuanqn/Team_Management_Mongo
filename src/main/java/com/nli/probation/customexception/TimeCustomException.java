package com.nli.probation.customexception;

/**
 * Custom exception about a range time in application
 */
public class TimeCustomException extends RuntimeException{
    public TimeCustomException() {
    }

    public TimeCustomException(String message) {
        super(message);
    }

    public TimeCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeCustomException(Throwable cause) {
        super(cause);
    }

    public TimeCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
