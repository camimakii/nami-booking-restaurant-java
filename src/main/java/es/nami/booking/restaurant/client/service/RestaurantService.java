package es.nami.booking.restaurant.client.service;

import es.nami.booking.restaurant.booking.BookingSettings;
import es.nami.booking.restaurant.booking.BookingSettingsRepository;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.data.RestaurantGroup;
import es.nami.booking.restaurant.client.data.RestaurantRepository;
import es.nami.booking.restaurant.error.NamiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {

    public static final String ENTITY_NAME = "Restaurant";

    private final RestaurantGroupService restaurantGroupService;
    private final RestaurantRepository restaurantRepository;
    private final BookingSettingsRepository bookingSettingsRepository;

    public Restaurant createNewRestaurant(Restaurant restaurant) {
        restaurant.setId(null);
        return restaurantRepository.save(restaurant);
    }

    public Restaurant findRestaurantById(Long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        return NamiException.orElseThrow(restaurantOptional, ENTITY_NAME, restaurantId.toString());
    }

    public void verifyRestaurantExists(long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw NamiException.notFound(ENTITY_NAME, restaurantId);
        }
    }

    public List<Restaurant> findRestaurantsByRestaurantGroup(Long groupId) {
        RestaurantGroup group = restaurantGroupService.findRestaurantGroupById(groupId);
        return restaurantRepository.findAllByRestaurantGroup(group);
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        verifyRestaurantExists(restaurant.getId());
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurantAndCascade(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    // TODO: to move later
    public BookingSettings findSettingsByRestaurant(Restaurant restaurant) {
        Optional<BookingSettings> bookingSettingsOptional = bookingSettingsRepository.findOneByRestaurant(restaurant);
        return NamiException.orElseThrow(bookingSettingsOptional, "BookingSettings for restaurant", restaurant.getId().toString());
    }


}
