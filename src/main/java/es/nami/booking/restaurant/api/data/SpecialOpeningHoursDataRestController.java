package es.nami.booking.restaurant.api.data;

import es.nami.booking.restaurant.api.error.ErrorJson;
import es.nami.booking.restaurant.core.data.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
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
@RequestMapping("/api/special-opening-hours")
@Slf4j
@RequiredArgsConstructor
public class SpecialOpeningHoursDataRestController {

    private final SpecialOpeningHoursDataService specialOpeningHoursDataService;

    @PostMapping
    @Operation(summary = "POST New Special Opening Hours", description = "Create a new exceptional sequence of opening hours for a specific date that will override the usual opening hours.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request: JSON invalid", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = ErrorJson.class))}),
    })
    public ResponseEntity createNewSpecialOpeningHours(
            @RequestBody SpecialOpeningHours specialOpeningHours
    ) {
        return new ResponseEntity(specialOpeningHoursDataService.createOneSpecialOpeningHours(specialOpeningHours), HttpStatus.CREATED);
    }

}
