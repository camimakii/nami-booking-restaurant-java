package es.nami.booking.restaurant.api.data;

import es.nami.booking.restaurant.core.data.OpeningHoursDataService;
import es.nami.booking.restaurant.data.opening.OpeningHours;
import es.nami.booking.restaurant.util.JsonUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/opening-hours")
@Slf4j
@RequiredArgsConstructor
public class OpeningHoursDataRestController {

    private final OpeningHoursDataService openingHoursDataService;

    @PostMapping
    @Operation(summary = "POST OpeningHours", description = "Add a new sequence of hours of opening of a restaurant with a start time and a duration")
    @ApiResponse(responseCode = "201", description = "Successfully created")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createNewOpeningHours(
            @RequestBody OpeningHours openingHours,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} body:\n{}", request.getMethod(), request.getRequestURI(), JsonUtil.toJson(openingHours));
        return ResponseEntity.status(HttpStatus.CREATED).body(openingHoursDataService.createOneOpeningHours(openingHours));
    }

    @GetMapping("/all")
    @Operation(summary = "GET all Opening Hours of a Restaurant", description = "Retrieve all the periods where the restaurant is open")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<OpeningHours>> getAllOpeningHoursOfRestaurant(
            @RequestParam long restaurantId,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.ok(openingHoursDataService.findOpeningHoursByRestaurant(restaurantId));
    }

    @PutMapping
    @Operation(summary = "UPDATE OpenHours", description = "Update one period of Opening Hours of a restaurant)")
    @ApiResponse(responseCode = "200", description = "Successfully updated")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OpeningHours> updateOpeningHours(
            @RequestBody OpeningHours openingHours,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} body:\n{}", request.getMethod(), request.getRequestURI(), JsonUtil.toJson(openingHours));
        return ResponseEntity.ok(openingHoursDataService.updateOpeningHours(openingHours));
    }

    @DeleteMapping(value = "/{openingHoursId}")
    @Operation(summary = "DELETE OpeningHours", description = "Delete a period of hours where the restaurant is open")
    @ApiResponse(responseCode = "204", description = "Successfully deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteOpeningHours(
            @PathVariable Long openingHoursId,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} id:{}", request.getMethod(), request.getRequestURI(), openingHoursId);
        openingHoursDataService.deleteOneOpeningHours(openingHoursId);
        return ResponseEntity.noContent().build();
    }

}
