package com.nli.probation.customexception;

/**
 * Custom exception about a class(like exception abount field name,...)
 */
public class ClassCustomException extends RuntimeException{
    public ClassCustomException() {
    }

    public ClassCustomException(String message) {
        super(message);
    }

    public ClassCustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassCustomException(Throwable cause) {
        super(cause);
    }

    public ClassCustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
