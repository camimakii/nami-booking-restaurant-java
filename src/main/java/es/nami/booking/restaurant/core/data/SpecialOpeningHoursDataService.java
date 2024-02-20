package es.nami.booking.restaurant.core.data;

import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHoursRepository;
import es.nami.booking.restaurant.util.StreamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialOpeningHoursDataService {

    private final SpecialOpeningHoursRepository specialOpeningHoursRepository;

    public List<SpecialOpeningHours> findSpecialOpeningHoursForADate(Restaurant restaurant, LocalDate date) {
        return specialOpeningHoursRepository.findAllByRestaurantAndStartDateTimeBetween(restaurant, date.atStartOfDay(), date.atTime(23, 59, 59));
    }

    public List<SpecialOpeningHours> findSpecialOpeningHoursByIds(List<Long> ids) {
        return StreamUtil.transformIterableToList(specialOpeningHoursRepository.findAllById(ids));
    }

    public SpecialOpeningHours createOneSpecialOpeningHours(SpecialOpeningHours specialOpeningHours) {
        return specialOpeningHoursRepository.save(specialOpeningHours);
    }

    public boolean deleteOneSpecialOpeningHours(long specialOpeningHoursId) {
        specialOpeningHoursRepository.deleteById(specialOpeningHoursId);
        return true;
    }

}
