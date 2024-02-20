package es.nami.booking.restaurant.core.data;

import es.nami.booking.restaurant.data.booking.BookingSettings;
import es.nami.booking.restaurant.data.booking.BookingSettingsRepository;
import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.data.restaurant.RestaurantGroup;
import es.nami.booking.restaurant.data.restaurant.RestaurantRepository;
import es.nami.booking.restaurant.exception.NamiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantDataService {

    public static final String ENTITY_NAME = "Restaurant";

    private final RestaurantGroupDataService restaurantGroupDataService;
    private final RestaurantRepository restaurantRepository;
    private final BookingSettingsRepository bookingSettingsRepository;

    public Restaurant createNewRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant findRestaurantById(long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        return NamiException.ifNotFound(restaurantOptional, ENTITY_NAME, restaurantId);
    }

    public List<Restaurant> findRestaurantsByRestaurantGroup(Long groupId) {
        RestaurantGroup group = restaurantGroupDataService.findRestaurantGroupById(groupId);
        return restaurantRepository.findAllByRestaurantGroup(group);
    }

    public Restaurant updateRestaurant(Restaurant restaurant) {
        if (!restaurantRepository.existsById(restaurant.getId())) {
            throw NamiException.notFoundConstructor(ENTITY_NAME, restaurant.getId());
        }
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurantAndCascade(Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        restaurantRepository.delete(restaurant);
    }

    // TODO: to move later
    public BookingSettings findSettingsByRestaurant(Restaurant restaurant) {
        Optional<BookingSettings> bookingSettingsOptional = bookingSettingsRepository.findOneByRestaurant(restaurant);
        return NamiException.ifNotFound(bookingSettingsOptional, "BookingSettings for restaurant", restaurant.getId());
    }


}
