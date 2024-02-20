package es.nami.booking.restaurant.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class NamiException extends RuntimeException {

    private ErrorCode errorCode;
    private String additionalMessage;

    public NamiException(ErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }

    public NamiException(ErrorCode errorCode, String additionalMessage) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
        this.additionalMessage = additionalMessage;
    }

    public static <T> T ifNotFound(Optional<T> optional, String entityName, long id) {
        return optional.orElseThrow(() ->
                new NamiException(ErrorCode.NOT_FOUND, entityName + " with id [" + id + "] not found"));
    }

    public static NamiException notFoundConstructor(String entityName, long id) {
        return new NamiException(ErrorCode.NOT_FOUND, entityName + " with id [" + id + "] not found");
    }

}
