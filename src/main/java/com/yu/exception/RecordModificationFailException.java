package com.yu.exception;

public class RecordModificationFailException extends RuntimeException {

    public RecordModificationFailException() {
    }

    public RecordModificationFailException(String message) {
        super(message);
    }

    public RecordModificationFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordModificationFailException(Throwable cause) {
        super(cause);
    }

}
