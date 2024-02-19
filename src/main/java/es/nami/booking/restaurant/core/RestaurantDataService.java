package es.nami.booking.restaurant.core;

import es.nami.booking.restaurant.data.Restaurant;
import es.nami.booking.restaurant.data.RestaurantGroup;
import es.nami.booking.restaurant.data.RestaurantRepository;
import es.nami.booking.restaurant.data.booking.BookingSettings;
import es.nami.booking.restaurant.data.booking.BookingSettingsRepository;
import es.nami.booking.restaurant.data.opening.OpeningHoursRepository;
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

    private final RestaurantGroupDataService restaurantGroupDataService;
    private final RestaurantRepository restaurantRepository;
    private final BookingSettingsRepository bookingSettingsRepository;
    private final OpeningHoursRepository openingHoursRepository;

    public Restaurant findRestaurantById(long restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        return NamiException.ifNotFound(restaurantOptional, "Restaurant", restaurantId);
    }

    public List<Restaurant> findRestaurantsByRestaurantGroup(Long groupId) {
        RestaurantGroup group = restaurantGroupDataService.findRestaurantGroupById(groupId);
        return restaurantRepository.findAllByRestaurantGroup(group);
    }

    public BookingSettings findSettingsByRestaurant(Restaurant restaurant) {
        Optional<BookingSettings> bookingSettingsOptional = bookingSettingsRepository.findOneByRestaurant(restaurant);
        return NamiException.ifNotFound(bookingSettingsOptional, "BookingSettings for restaurant", restaurant.getId());
    }


}
