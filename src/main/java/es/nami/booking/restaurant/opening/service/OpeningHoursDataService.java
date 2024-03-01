package es.nami.booking.restaurant.opening.service;

import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.service.RestaurantDataService;
import es.nami.booking.restaurant.error.NamiException;
import es.nami.booking.restaurant.opening.data.OpeningHours;
import es.nami.booking.restaurant.opening.data.OpeningHoursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpeningHoursDataService {

    public static final String ENTITY_NAME = "OpeningHours";

    private final OpeningHoursRepository openingHoursRepository;
    private final RestaurantDataService restaurantDataService;

    public OpeningHours createOneOpeningHours(OpeningHours openingHours) {
        openingHours.setId(null);
        return openingHoursRepository.save(openingHours);
    }

    public OpeningHours findOpeningHoursById(Long openingHoursId) {
        Optional<OpeningHours> openingHoursOptional = openingHoursRepository.findById(openingHoursId);
        return NamiException.orElseThrow(openingHoursOptional, ENTITY_NAME, openingHoursId.toString());
    }

    public void verifyOpeningHoursExists(long openingHoursId) {
        if (!openingHoursRepository.existsById(openingHoursId)) {
            throw NamiException.notFound(ENTITY_NAME, openingHoursId);
        }
    }

    public List<OpeningHours> findOpeningHoursByRestaurant(long restaurantId) {
        return openingHoursRepository.findAllByRestaurant(restaurantDataService.findRestaurantById(restaurantId));
    }

    public List<OpeningHours> findOpeningHoursByRestaurantAndDayOfWeek(Restaurant restaurant, DayOfWeek dayOfWeek) {
        return openingHoursRepository.findAllByRestaurantAndDayOfWeek(restaurant, dayOfWeek);
    }

    public OpeningHours updateOpeningHours(OpeningHours openingHours) {
        verifyOpeningHoursExists(openingHours.getId());
        return openingHoursRepository.save(openingHours);
    }

    public void deleteOneOpeningHours(long openingHoursId) {
        OpeningHours openingHours = findOpeningHoursById(openingHoursId);
        openingHoursRepository.delete(openingHours);
    }

}
