package es.nami.booking.restaurant.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class NamiException extends RuntimeException {

    private ErrorCode errorCode;
    private String additionalMessage;

    public static <T> T ifNotFound(Optional<T> optional, String entityName, long id) {
        return optional.orElseThrow(() ->
                new NamiException(ErrorCode.NOT_FOUND, entityName + " with id [" + id + "] not found"));
    }

    public static NamiException notFoundConstructor(String entityName, long id) {
        return new NamiException(ErrorCode.NOT_FOUND, entityName + " with id [" + id + "] not found");
    }

}
