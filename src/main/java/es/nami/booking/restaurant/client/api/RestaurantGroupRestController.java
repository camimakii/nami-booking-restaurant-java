package es.nami.booking.restaurant.client.api;

import es.nami.booking.restaurant.client.data.RestaurantGroup;
import es.nami.booking.restaurant.client.dto.CreateRestaurantGroupRequest;
import es.nami.booking.restaurant.client.dto.CreateRestaurantGroupResponse;
import es.nami.booking.restaurant.client.service.RestaurantGroupService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "restaurant-group")
@RequiredArgsConstructor // To replace Autowired on constructor
@Slf4j
public class RestaurantGroupRestController {

    private final RestaurantGroupService restaurantGroupService;

    @PostMapping("/new")
    @Operation(summary = "POST New RestaurantGroup", description = "Create a new group of restaurants with its admin user in DB and returns it with its generated ID")
    @ApiResponse(responseCode = "201", description = "Successfully created")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreateRestaurantGroupResponse> createNewRestaurantGroup(
            @RequestBody CreateRestaurantGroupRequest createRestaurantGroupRequest,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {}", request.getMethod(), request.getRequestURI()); // No log of password
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantGroupService.createNewRestaurantGroup(createRestaurantGroupRequest));
    }

    @GetMapping("/{restaurantGroupId}")
    @Operation(summary = "GET one RestaurantGroup", description = "Retrieve informations of one group of restaurants")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RestaurantGroup> getOneRestaurantGroup(
            @PathVariable Long restaurantGroupId,
            HttpServletRequest request
    ) {

        log.debug("[REQUEST] {} {} id:{}", request.getMethod(), request.getRequestURI(), restaurantGroupId);
        return ResponseEntity.ok(restaurantGroupService.findRestaurantGroupById(restaurantGroupId));

    }

    @GetMapping("/all")
    @Operation(summary = "GET all groups", description = "Retrieve all groups of restaurants")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<RestaurantGroup>> getAllRestaurantGroups(
            HttpServletRequest request
    ) {

        log.debug("[REQUEST] {} {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.ok(restaurantGroupService.findAllRestaurantGroups());

    }

    @PutMapping
    @Operation(summary = "UPDATE RestaurantGroup", description = "Update informations of a group of restaurants)")
    @ApiResponse(responseCode = "200", description = "Successfully updated")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RestaurantGroup> updateRestaurantGroup(
            @RequestBody RestaurantGroup restaurantGroup,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} body:\n{}", request.getMethod(), request.getRequestURI(), JsonUtil.toJson(restaurantGroup));
        return ResponseEntity.ok(restaurantGroupService.updateRestaurantGroup(restaurantGroup));

    }

    @DeleteMapping(value = "/{restaurantGroupId}")
    @Operation(summary = "DELETE RestaurantGroup", description = "Delete a group of restaurants in DB by its ID and all its related informations (restaurants, bookings, settings, ...)")
    @ApiResponse(responseCode = "204", description = "Successfully deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity deleteRestaurantGroup(
            @PathVariable Long restaurantGroupId,
            HttpServletRequest request
    ) {

        log.debug("[REQUEST] {} {} id:{}", request.getMethod(), request.getRequestURI(), restaurantGroupId);
        restaurantGroupService.deleteRestaurantGroupAndCascade(restaurantGroupId);
        return ResponseEntity.noContent().build();

    }

}
