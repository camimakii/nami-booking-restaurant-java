package es.nami.booking.restaurant.core.availabilities;

import es.nami.booking.restaurant.core.data.OpeningHoursDataService;
import es.nami.booking.restaurant.core.data.RestaurantDataService;
import es.nami.booking.restaurant.core.data.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.data.opening.OpeningHours;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.dto.DayOfMonth;
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
    private final OpeningHoursDataService openingHoursDataService;
    private final SpecialOpeningHoursDataService specialOpeningHoursDataService;

    public List<DayOfMonth> getDaysAvailabilitiesOfMonthForRestaurant(long restaurantId, int year, int month, LocalDate localDate) {
        Restaurant restaurant = restaurantDataService.findRestaurantById(restaurantId);
        List<LocalDate> daysOfMonth = DatesUtil.getDatesForMonthAndYear(year, month);
        List<DayOfMonth> days = new ArrayList<>();
        for (LocalDate date : daysOfMonth) {
            // 1. CHECK IF DAY IS PASSED
            DayOfMonth opening = DayOfMonth.builder()
                    .date(date)
                    .isPassed(date.isBefore(localDate))
                    .specialOpeningHoursId(new ArrayList<>())
                    .build();
            if (!opening.isPassed()) {
                // 2. FIND THE USUAL OPENING HOURS
                List<OpeningHours> openingHours = openingHoursDataService.findOpeningHoursByRestaurantAndDayOfWeek(restaurant, date.getDayOfWeek());
                for (OpeningHours openingHour : openingHours) {
                    if (openingHour.isOpen()) {
                        opening.setOpen(true);
                        break;
                    }
                }
                // 3. CHECK IF THERE ARE EXCEPTIONAL OPENING HOURS FOR THAT DAY, IF YES, IT OVERRIDES THE USUAL ONE
                List<SpecialOpeningHours> specialOpeningHours = specialOpeningHoursDataService.findSpecialOpeningHoursForADate(restaurant, date);
                if (!specialOpeningHours.isEmpty()) {
                    opening.setWithSpecialOpeningHours(true);
                    opening.setOpen(false);
                    opening.setSpecialOpeningHoursId(
                            specialOpeningHours
                                    .stream()
                                    .map(specialOpeningHoursItem -> specialOpeningHoursItem.getId())
                                    .collect(Collectors.toList()));
                    for (SpecialOpeningHours specialOpeningHour : specialOpeningHours) {
                        if (specialOpeningHour.isOpen()) {
                            opening.setOpen(true);
                            break;
                        }
                    }
                }
//                    }

            }
            days.add(opening);
        }
        return days;
    }

}
