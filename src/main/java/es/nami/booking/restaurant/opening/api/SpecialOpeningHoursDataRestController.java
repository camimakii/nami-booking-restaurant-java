package es.nami.booking.restaurant.opening.api;

import es.nami.booking.restaurant.opening.data.SpecialOpeningHours;
import es.nami.booking.restaurant.opening.service.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.util.Constants;
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
@RequestMapping(Constants.API_URL + "special-opening-hours")
@Slf4j
@RequiredArgsConstructor
public class SpecialOpeningHoursDataRestController {

    private final SpecialOpeningHoursDataService specialOpeningHoursDataService;

    @PostMapping
    @Operation(summary = "POST New Special Opening Hours", description = "Create a new exceptional sequence of opening hours for a specific date that will override the usual opening hours.")
    @ApiResponse(responseCode = "201", description = "Successfully created")
    public ResponseEntity<SpecialOpeningHours> createNewSpecialOpeningHours(
            @RequestBody SpecialOpeningHours specialOpeningHours
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(specialOpeningHoursDataService.createOneSpecialOpeningHours(specialOpeningHours));
    }

    @GetMapping("/all")
    @Operation(summary = "GET all Special Opening Hours of a Restaurant", description = "Retrieve all the exceptional periods where the restaurant is open")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<SpecialOpeningHours>> getAllSpecialOpeningHoursOfRestaurant(
            @RequestParam long restaurantId,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.ok(specialOpeningHoursDataService.findSpecialOpeningHoursByRestaurant(restaurantId));
    }

    @PutMapping
    @Operation(summary = "UPDATE SpecialOpeningHours", description = "Update one exceptional period of Opening Hours of a restaurant)")
    @ApiResponse(responseCode = "200", description = "Successfully updated")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SpecialOpeningHours> updateSpecialOpeningHours(
            @RequestBody SpecialOpeningHours specialOpeningHours,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} body:\n{}", request.getMethod(), request.getRequestURI(), JsonUtil.toJson(specialOpeningHours));
        return ResponseEntity.ok(specialOpeningHoursDataService.updateSpecialOpeningHours(specialOpeningHours));
    }

    @DeleteMapping(value = "/{specialOpeningHoursId}")
    @Operation(summary = "DELETE SpecialOpeningHours", description = "Delete an exceptional period of hours where the restaurant is open")
    @ApiResponse(responseCode = "204", description = "Successfully deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteSpecialOpeningHours(
            @PathVariable Long specialOpeningHoursId,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} id:{}", request.getMethod(), request.getRequestURI(), specialOpeningHoursId);
        specialOpeningHoursDataService.deleteOneSpecialOpeningHours(specialOpeningHoursId);
        return ResponseEntity.noContent().build();
    }

}
