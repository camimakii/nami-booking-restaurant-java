package es.nami.booking.restaurant.api.data;

import es.nami.booking.restaurant.api.error.ErrorJson;
import es.nami.booking.restaurant.core.data.ClosureDayDataService;
import es.nami.booking.restaurant.data.opening.ClosureDay;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/closure-day")
@Slf4j
@RequiredArgsConstructor
public class ClosureDayDataRestController {

    private final ClosureDayDataService closureDayDataService;

    @PostMapping
    @Operation(summary = "POST New Closure Day", description = "Create a new closure day for a specific date that will override the usual opening hours.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request: JSON invalid", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorJson.class))}),
    })
    public ResponseEntity createNewClosureDay(
            @RequestBody ClosureDay closureDay
    ) {
        return new ResponseEntity(closureDayDataService.createOneClosureDay(closureDay), HttpStatus.CREATED);
    }

}
