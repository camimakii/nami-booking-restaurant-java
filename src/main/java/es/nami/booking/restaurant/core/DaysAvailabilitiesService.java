package es.nami.booking.restaurant.core;

import es.nami.booking.restaurant.data.Restaurant;
import es.nami.booking.restaurant.data.opening.OpeningHours;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
import es.nami.booking.restaurant.dto.DayOfMonthOpening;
import es.nami.booking.restaurant.util.DatesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DaysAvailabilitiesService {

    private final RestaurantDataService restaurantDataService;
    private final OpeningDataService openingDataService;

    public List<DayOfMonthOpening> getDaysAvailabilitiesOfMonthForRestaurant(long restaurantId, int year, int month) {
        Restaurant restaurant = restaurantDataService.findRestaurantById(restaurantId);
        List<LocalDate> daysOfMonth = DatesUtil.getDatesForMonthAndYear(year, month);
        List<DayOfMonthOpening> days = new ArrayList<>();
        for (LocalDate date : daysOfMonth) {
            DayOfMonthOpening opening = DayOfMonthOpening.builder()
                    .date(date)
                    .isPassed(LocalDate.now().isAfter(date))
                    .build();
            if (!opening.isPassed()) {
                opening.setOpen(openingDataService.findClosureDayForRestaurantAndDate(restaurant, date).isEmpty());
                if (opening.isOpen()) {
                    List<OpeningHours> openingHours = openingDataService.findOpeningHoursByRestaurantAndDayOfWeek(restaurant, date.getDayOfWeek());
                    for (OpeningHours openingHour : openingHours) {
                        if (!openingHour.isOpen()) {
                            opening.setOpen(false);
                            break;
                        }
                    }
                    if (opening.isOpen()) {
                        List<SpecialOpeningHours> specialOpeningHours = openingDataService.findSpecialOpeningHoursForADate(restaurant, date);
                        if (!specialOpeningHours.isEmpty()) {
                            opening.setWithSpecialOpeningHours(true);
                            opening.setSpecialOpeningHoursId(
                                    specialOpeningHours
                                            .stream()
                                            .map(specialOpeningHoursItem -> specialOpeningHoursItem.getId())
                                            .collect(Collectors.toList()));
                        }
                    }
                }
            }
            days.add(opening);
        }
        return days;
    }

}
