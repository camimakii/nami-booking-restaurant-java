package es.nami.booking.restaurant.core.data;

import es.nami.booking.restaurant.data.opening.OpeningHours;
import es.nami.booking.restaurant.data.opening.OpeningHoursRepository;
import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.exception.NamiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpeningHoursDataService {

    public static final String ENTITY_NAME = "OpeningHours";

    private final OpeningHoursRepository openingHoursRepository;
    private final RestaurantDataService restaurantDataService;

    public List<OpeningHours> findOpeningHoursByRestaurant(long restaurantId) {
        return openingHoursRepository.findAllByRestaurant(restaurantDataService.findRestaurantById(restaurantId));
    }

    public List<OpeningHours> findOpeningHoursByRestaurantAndDayOfWeek(Restaurant restaurant, DayOfWeek dayOfWeek) {
        return openingHoursRepository.findAllByRestaurantAndDayOfWeek(restaurant, dayOfWeek);
    }

    public OpeningHours createOneOpeningHours(OpeningHours openingHours) {
        return openingHoursRepository.save(openingHours);
    }

    public OpeningHours updateOpeningHours(OpeningHours openingHours) {
        if (!openingHoursRepository.existsById(openingHours.getId())) {
            throw NamiException.notFoundConstructor(ENTITY_NAME, openingHours.getId());
        }
        return openingHoursRepository.save(openingHours);
    }

    public boolean deleteOneOpeningHours(long openHoursId) {
        openingHoursRepository.deleteById(openHoursId);
        return true;
    }

}
