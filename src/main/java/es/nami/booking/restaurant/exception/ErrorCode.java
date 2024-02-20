package es.nami.booking.restaurant.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND("NAMI_404_ITEM", HttpStatus.NOT_FOUND, "Item not found"),
    INTERNAL_DB("NAMI_500_DB", HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while contacting the database"),
    INTERNAL_JSON("NAMI_500_JSON", HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while serializing Json (JsonUtil)");

    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(String errorCode, HttpStatus httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

}