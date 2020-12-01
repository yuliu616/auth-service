package com.yu.exception;

public class SecurityRiskException extends RuntimeException {

    public SecurityRiskException() {
    }

    public SecurityRiskException(String message) {
        super(message);
    }

    public SecurityRiskException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecurityRiskException(Throwable cause) {
        super(cause);
    }

}
