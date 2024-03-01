package es.nami.booking.restaurant.opening.service;

import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.service.RestaurantDataService;
import es.nami.booking.restaurant.error.NamiException;
import es.nami.booking.restaurant.opening.data.SpecialOpeningHours;
import es.nami.booking.restaurant.opening.data.SpecialOpeningHoursRepository;
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

    public SpecialOpeningHours findSpecialOpeningHoursById(Long specialOpeningHoursId) {
        Optional<SpecialOpeningHours> specialOpeningHoursOptional = specialOpeningHoursRepository.findById(specialOpeningHoursId);
        return NamiException.orElseThrow(specialOpeningHoursOptional, ENTITY_NAME, specialOpeningHoursId.toString());
    }

    public void verifySpecialOHExists(long specialOHId) {
        if (!specialOpeningHoursRepository.existsById(specialOHId)) {
            throw NamiException.notFound(ENTITY_NAME, specialOHId);
        }
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

    public SpecialOpeningHours updateSpecialOpeningHours(SpecialOpeningHours specialOH) {
        verifySpecialOHExists(specialOH.getId());
        return specialOpeningHoursRepository.save(specialOH);
    }

    public void deleteOneSpecialOpeningHours(long specialOHId) {
        SpecialOpeningHours specialOpeningHours = findSpecialOpeningHoursById(specialOHId);
        specialOpeningHoursRepository.delete(specialOpeningHours);
    }

}
