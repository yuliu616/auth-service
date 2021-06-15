package com.yu.controller;

import com.yu.exception.AuthenticationFailureException;
import com.yu.exception.InconsistencyDataException;
import com.yu.exception.RecordInsertionFailException;
import com.yu.exception.RecordModificationFailException;
import com.yu.exception.RecordNotFoundException;
import com.yu.exception.SecurityRiskException;
import com.yu.exception.UnhandledException;
import com.yu.model.dto.ErrorCodeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_INVALID_USE_ERROR = "INVALID_USE_ERROR";

    public static final String ERROR_VALIDATION_ERROR = "VALIDATION_ERROR";

    public static final String ERROR_INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    public static final String ERROR_UNKNOWN_ERROR = "UNKNOWN_ERROR";

    public static final String ERROR_RECORD_NOT_FOUND = "RECORD_NOT_FOUND";

    public static final String ERROR_AUTHENTICATION_ERROR = "AUTHENTICATION_ERROR";

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SecurityRiskException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto securityRisk(SecurityRiskException exception){
        logger.warn("securityRisk: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_VALIDATION_ERROR);
    }

    @ExceptionHandler(java.sql.SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto sqlLevelException(java.sql.SQLIntegrityConstraintViolationException exception){
        logger.warn("sqlLevelException: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RecordInsertionFailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto insertionFailure(RecordInsertionFailException exception){
        logger.warn("insertionFailure: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnhandledException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto unknownError(UnhandledException exception){
        logger.warn("unknownError: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_UNKNOWN_ERROR);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto recordNotFound(RecordNotFoundException exception){
        logger.warn("recordNotFound: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_RECORD_NOT_FOUND);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto mvcValidationFailure(org.springframework.web.bind.MethodArgumentNotValidException exception){
        logger.warn("mvcValidationFailure: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_VALIDATION_ERROR);
    }

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto mvcWrongTypeFailure(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException exception){
        logger.warn("mvcWrongTypeFailure: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_VALIDATION_ERROR);
    }

    @ExceptionHandler(InconsistencyDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto invalidCallFailure(InconsistencyDataException exception){
        logger.warn("invalidCallFailure: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_INVALID_USE_ERROR);
    }

    @ExceptionHandler(RecordModificationFailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto invalidCallFailure(RecordModificationFailException exception){
        logger.warn("invalidCallFailure: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_INVALID_USE_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto validationFailure(ValidationException exception){
        logger.warn("validationFailure: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_VALIDATION_ERROR);
    }

    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto httpBodyParsingError(org.springframework.http.converter.HttpMessageNotReadableException exception){
        logger.warn("httpBodyParsingError: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_INVALID_USE_ERROR);
    }

    @ExceptionHandler(AuthenticationFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCodeDto authenticationFail(AuthenticationFailureException exception){
        logger.warn("authenticationFail: {}", exception.getMessage(), exception);
        return new ErrorCodeDto(ERROR_AUTHENTICATION_ERROR);
    }

}
