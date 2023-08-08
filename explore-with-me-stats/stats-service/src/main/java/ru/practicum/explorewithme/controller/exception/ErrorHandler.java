package ru.practicum.explorewithme.controller.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLocalDateTimeException(final LocalDateTimeException e) {
        e.printStackTrace();
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleThrowableException(final Throwable e) {
        e.printStackTrace();
        return new ErrorResponse(e.getMessage());
    }
}

@Getter
class ErrorResponse {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}