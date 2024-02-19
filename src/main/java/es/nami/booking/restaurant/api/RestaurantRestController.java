package es.nami.booking.restaurant.api;

import es.nami.booking.restaurant.core.RestaurantDataService;
import es.nami.booking.restaurant.core.RestaurantGroupDataService;
import es.nami.booking.restaurant.data.Restaurant;
import es.nami.booking.restaurant.data.RestaurantGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant")
@Slf4j
@RequiredArgsConstructor
public class RestaurantRestController {

    private final RestaurantDataService restaurantDataService;
    private final RestaurantGroupDataService restaurantGroupDataService;

    @GetMapping(value = "/groups")
    public ResponseEntity<List<RestaurantGroup>> getAllRestaurntGroups() {
        return new ResponseEntity<>(
                restaurantGroupDataService.findAllRestaurantGroups(),
                HttpStatus.OK);
    }

    @GetMapping(value = "/group/{groupId}/restaurants")
    public ResponseEntity<List<Restaurant>> getRestaurntsOfGroup(
            @PathVariable Long groupId) {
        return new ResponseEntity<>(
                restaurantDataService.findRestaurantsByRestaurantGroup(groupId),
                HttpStatus.OK);
    }

}
