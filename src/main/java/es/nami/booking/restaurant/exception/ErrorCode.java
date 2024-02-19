package es.nami.booking.restaurant.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND("NAMI_404", HttpStatus.NOT_FOUND, "Item not found"),
    INTERNAL_DB("NAMI_500", HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while contacting the database");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(String errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}