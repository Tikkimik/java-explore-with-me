package ru.practicum.ewm.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectParameterException(final IncorrectParameterException e) {
        log.warn(e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUpdateException(final UpdateException e) {
        log.warn(e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleThrowableException(final Throwable e) {
        log.error(e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundParameterException(final NotFoundParameterException e) {
        log.warn(e.getMessage());
        return new ApiError(HttpStatus.NOT_FOUND, e.getMessage(), e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(final ConflictException e) {
        log.warn(e.getMessage());
        return new ApiError(HttpStatus.CONFLICT, e.getMessage(), e.getMessage());
    }

}

