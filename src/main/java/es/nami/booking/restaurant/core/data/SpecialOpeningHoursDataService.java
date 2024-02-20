package es.nami.booking.restaurant.core.data;

import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHoursRepository;
import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.exception.NamiException;
import es.nami.booking.restaurant.util.StreamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialOpeningHoursDataService {

    public static final String ENTITY_NAME = "SpecialOpeningHours";

    private final SpecialOpeningHoursRepository specialOpeningHoursRepository;
    private final RestaurantDataService restaurantDataService;

    public SpecialOpeningHours createOneSpecialOpeningHours(SpecialOpeningHours specialOpeningHours) {
        specialOpeningHours.setId(null);
        return specialOpeningHoursRepository.save(specialOpeningHours);
    }

    public SpecialOpeningHours findSpecialOpeningHoursById(long specialOpeningHoursId) {
        Optional<SpecialOpeningHours> specialOpeningHoursOptional = specialOpeningHoursRepository.findById(specialOpeningHoursId);
        return NamiException.ifNotFound(specialOpeningHoursOptional, ENTITY_NAME, specialOpeningHoursId);
    }

    public List<SpecialOpeningHours> findSpecialOpeningHoursByRestaurant(long restaurantId) {
        return specialOpeningHoursRepository.findAllByRestaurant(restaurantDataService.findRestaurantById(restaurantId));
    }

    public List<SpecialOpeningHours> findSpecialOpeningHoursForADate(Restaurant restaurant, LocalDate date) {
        return specialOpeningHoursRepository.findAllByRestaurantAndStartDateTimeBetween(restaurant, date.atStartOfDay(), date.atTime(23, 59, 59));
    }

    public List<SpecialOpeningHours> findSpecialOpeningHoursByIds(List<Long> ids) {
        return StreamUtil.transformIterableToList(specialOpeningHoursRepository.findAllById(ids));
    }

    public SpecialOpeningHours updateSpecialOpeningHours(SpecialOpeningHours openingHours) {
        if (!specialOpeningHoursRepository.existsById(openingHours.getId())) {
            throw NamiException.notFoundConstructor(ENTITY_NAME, openingHours.getId());
        }
        return specialOpeningHoursRepository.save(openingHours);
    }

    public void deleteOneSpecialOpeningHours(long specialOpeningHoursId) {
        SpecialOpeningHours specialOpeningHours = findSpecialOpeningHoursById(specialOpeningHoursId);
        specialOpeningHoursRepository.delete(specialOpeningHours);
    }

}
