package com.yu.exception;

public class UnhandledException extends RuntimeException {

    public UnhandledException() {
    }

    public UnhandledException(String message) {
        super(message);
    }

    public UnhandledException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnhandledException(Throwable cause) {
        super(cause);
    }

}
