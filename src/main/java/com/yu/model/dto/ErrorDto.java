package com.yu.model.dto;

public class ErrorDto extends ErrorCodeDto {

    protected Object[] args;

    public ErrorDto(String errorCode) {
        super(errorCode);
    }

    public ErrorDto(String errorCode, Object[] args) {
        super(errorCode);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
