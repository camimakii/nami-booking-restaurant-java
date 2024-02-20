package es.nami.booking.restaurant.api.error;

import es.nami.booking.restaurant.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorJson {

    private String errorCode;
    private String errorMessage;
    private String additionalMessage;

    public ErrorJson(ErrorCode errorCode) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorMessage();
    }

    public ErrorJson(ErrorCode errorCode, String additionalMessage) {
        this.errorCode = errorCode.getErrorCode();
        this.errorMessage = errorCode.getErrorMessage();
        this.additionalMessage = additionalMessage;
    }

}
