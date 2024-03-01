package es.nami.booking.restaurant.client.api;

import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.service.RestaurantService;
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
@RequestMapping(Constants.API_URL + "restaurant")
@RequiredArgsConstructor
@Slf4j
public class RestaurantRestController {

    private final RestaurantService restaurantService;

    @PostMapping
    @Operation(summary = "POST New Restaurant", description = "Create a new restaurant owned by a group")
    @ApiResponse(responseCode = "201", description = "Successfully created")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity createNewRestaurant(
            @RequestBody Restaurant restaurant,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} body:\n{}", request.getMethod(), request.getRequestURI(), JsonUtil.toJson(restaurant));
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createNewRestaurant(restaurant));
    }

    @GetMapping("/all")
    @Operation(summary = "GET all restaurants of RestaurantGroup", description = "Retrieve all restaurants owned by one group of restaurants")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Restaurant>> getAllRestaurantOfGroup(
            @RequestParam long restaurantGroupId,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.ok(restaurantService.findRestaurantsByRestaurantGroup(restaurantGroupId));
    }

    @GetMapping("/{restaurantId}")
    @Operation(summary = "GET one Restaurant", description = "Retrieve informations of one restaurant")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Restaurant> getOneRestaurant(
            @PathVariable Long restaurantId,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} id:{}", request.getMethod(), request.getRequestURI(), restaurantId);
        return ResponseEntity.ok(restaurantService.findRestaurantById(restaurantId));
    }

    @PutMapping
    @Operation(summary = "UPDATE Restaurant", description = "Update informations of a restaurant)")
    @ApiResponse(responseCode = "200", description = "Successfully updated")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Restaurant> updateRestaurant(
            @RequestBody Restaurant restaurant,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} body:\n{}", request.getMethod(), request.getRequestURI(), JsonUtil.toJson(restaurant));
        return ResponseEntity.ok(restaurantService.updateRestaurant(restaurant));
    }

    @DeleteMapping(value = "/{restaurantId}")
    @Operation(summary = "DELETE Restaurant", description = "Delete a restaurant in DB by its ID and all its related informations (bookings, settings, ...)")
    @ApiResponse(responseCode = "204", description = "Successfully deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteRestaurant(
            @PathVariable Long restaurantId,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} id:{}", request.getMethod(), request.getRequestURI(), restaurantId);
        restaurantService.deleteRestaurantAndCascade(restaurantId);
        return ResponseEntity.noContent().build();
    }

}
