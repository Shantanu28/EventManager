package com.cultur.eventmanager.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Created by shantanu on 29/4/17.
 */
@Data
public class EventManagerApiError {

    private HttpStatus status;
    private String message;
    private List<String> errors;

    public EventManagerApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public EventManagerApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
