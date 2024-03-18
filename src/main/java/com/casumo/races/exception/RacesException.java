package com.casumo.races.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class RacesException extends RuntimeException{

    private final String title;

    private final String message;

    private final HttpStatus statusCode;

}
