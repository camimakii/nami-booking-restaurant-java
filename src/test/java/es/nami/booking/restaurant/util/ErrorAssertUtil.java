package es.nami.booking.restaurant.util;

import es.nami.booking.restaurant.error.ErrorJson;
import es.nami.booking.restaurant.error.ErrorCode;
import lombok.SneakyThrows;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorAssertUtil {

    @SneakyThrows
    public static void assertErrorResponse(ErrorCode expectedError, MvcResult result, String... args) {
        // SETUP RESPONSE
        String jsonResponse = result.getResponse().getContentAsString();
        ErrorJson errorJson = JsonUtil.fromJson(jsonResponse, ErrorJson.class);

        // ASSERTIONS TO ENSURE WE GET THE CORRECT ERROR MESSAGE
        ErrorJson expectedJson = new ErrorJson(expectedError);
        assertEquals(expectedJson.getErrorCode(), errorJson.getErrorCode());
        assertEquals(expectedJson.getErrorMessage(), errorJson.getErrorMessage());
        if (args.length > 0) {
            assertNotNull(errorJson.getAdditionalMessage());
            assertFalse(errorJson.getAdditionalMessage().isEmpty());
            for (int i = 0; i < args.length; i++) {
                assertTrue(errorJson.getAdditionalMessage().contains(args[i]));
            }
        }
    }

}
