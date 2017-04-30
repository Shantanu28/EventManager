package com.cultur.eventmanager.enums;

/**
 * Created by Shantanu on 20/Apr/17.
 * Enum for various status maintained in application.
 */
public enum ResponseStatus {
    SUCCESS("SUCCESS"),
    INTERNAL_ERROR("An internal error has occurred. Please try again later"),
    EMAIL_ALREADY_REGISTERED("EMAIL_ALREADY_REGISTERED"),
    EMAIL_NOT_REGISTERED("EMAIL_NOT_REGISTERED"),
    INVALID_FIELD("INVALID_FIELD"),
    INVALID_REQUEST("INVALID_REQUEST"),
    USER_NOT_FOUND("USER_NOT_FOUND"),
    ACTIVATION_PENDING("ACTIVATION_PENDING"),
    LOGIN_FAILED("LOGIN_FAILED"),
    LOGIN_SUCCESS("LOGIN_SUCCESS"),
    PASSWORD_MISMATCH("PASSWORD_MISMATCH"),
    LOGOUT_SUCCESS("LOGOUT_SUCCESS"),
    INVALID_AUTH_TOKEN("INVALID_AUTH_TOKEN"),
    PERSON_NOT_FOUND("PERSON_NOT_FOUND"),
    NOT_FOUND("NOT_FOUND"),
    FOUND("FOUND"),
    ALREADY_PRESENT("ALREADY_PRESENT"),
    INVALID_ACTIVATION_TOKEN("INVALID_ACTIVATION_TOKEN"),
    EMAIL_SENDING_FAILED("EMAIL_SENDING_FAILED");

    private final String status;

    ResponseStatus(String status) {
        this.status = status;
    }

    public String getStatus(){
        return this.status;
    }
}
