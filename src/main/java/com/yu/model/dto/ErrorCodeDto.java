package com.yu.model.dto;

public class ErrorCodeDto {

    protected String errorCode;

    public ErrorCodeDto(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
