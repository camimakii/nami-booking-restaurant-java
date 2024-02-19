package es.nami.booking.restaurant.api;

import es.nami.booking.restaurant.exception.ErrorCode;
import es.nami.booking.restaurant.exception.NamiException;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NamiException.class})
    protected ResponseEntity<Object> handleError(NamiException ex, WebRequest request) {
        return new ResponseEntity<>(new ErrorJson(ex.getErrorCode()), ex.getErrorCode().getHttpStatus());
    }
}

@Getter
class ErrorJson {

    private String errorCode;
    private String errorMessage;

    public ErrorJson(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorMessage();
    }

}