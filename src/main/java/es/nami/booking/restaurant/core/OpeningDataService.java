package es.nami.booking.restaurant.core;

import es.nami.booking.restaurant.data.Restaurant;
import es.nami.booking.restaurant.data.opening.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpeningDataService {

    private final OpeningHoursRepository openingHoursRepository;
    private final SpecialOpeningHoursRepository specialOpeningHoursRepository;
    private final ClosureDayRepository closureDayRepository;

    public List<OpeningHours> findOpeningHoursByRestaurantAndDayOfWeek(Restaurant restaurant, DayOfWeek dayOfWeek) {
        return openingHoursRepository.findAllByRestaurantAndDayOfWeek(restaurant, dayOfWeek);
    }

    public List<SpecialOpeningHours> findSpecialOpeningHoursForADate(Restaurant restaurant, LocalDate date) {
        return specialOpeningHoursRepository.findAllByRestaurantAndStartDateTimeBetween(restaurant, date.atStartOfDay(), date.atTime(23, 59, 59));
    }

    public Optional<ClosureDay> findClosureDayForRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return closureDayRepository.findOneByRestaurantAndDate(restaurant, date);
    }

    public List<SpecialOpeningHours> findSpecialOpeningHoursByIds(List<Long> ids) {
        return StreamSupport
                .stream(specialOpeningHoursRepository.findAllById(ids).spliterator(), false)
                .collect(Collectors.toList());
    }
}
