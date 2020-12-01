package com.yu.exception;

public class InconsistencyDataException extends RuntimeException {

    public InconsistencyDataException() {
    }

    public InconsistencyDataException(String message) {
        super(message);
    }

    public InconsistencyDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InconsistencyDataException(Throwable cause) {
        super(cause);
    }

}
