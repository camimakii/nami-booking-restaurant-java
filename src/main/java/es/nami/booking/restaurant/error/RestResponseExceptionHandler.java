package es.nami.booking.restaurant.error;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseExceptionHandler
        extends ResponseEntityExceptionHandler {

    //    org.postgresql.util.PSQLException
//    AuthenticationException
//    io.jsonwebtoken.MalformedJwtException
    @ExceptionHandler(value = {NamiException.class})
    protected ResponseEntity<Object> handleError(NamiException ex, WebRequest request) {
        if (ex.getAdditionalMessage() != null && !ex.getAdditionalMessage().isEmpty()) {
            return new ResponseEntity<>(new ErrorJson(ex.getErrorCode(), ex.getAdditionalMessage()), ex.getErrorCode().getHttpStatus());
        }
        return new ResponseEntity<>(new ErrorJson(ex.getErrorCode()), ex.getErrorCode().getHttpStatus());
    }
}

