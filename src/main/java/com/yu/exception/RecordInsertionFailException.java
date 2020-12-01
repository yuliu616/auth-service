package com.yu.exception;

public class RecordInsertionFailException extends RuntimeException {

    public RecordInsertionFailException() {
    }

    public RecordInsertionFailException(String message) {
        super(message);
    }

    public RecordInsertionFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordInsertionFailException(Throwable cause) {
        super(cause);
    }

}
