package com.casumo.races.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RacesException.class})
    protected ResponseEntity<ErrorResponse> racesExceptionHandler(
            RacesException ex, WebRequest request) {
        var response = new ErrorResponse(ex.getTitle(), ex.getMessage());
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception ex) {
        var response = new ErrorResponse("Unhandled exception", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
