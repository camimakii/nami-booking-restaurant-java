package es.nami.booking.restaurant.core;

import es.nami.booking.restaurant.data.Restaurant;
import es.nami.booking.restaurant.data.booking.BookingSettings;
import es.nami.booking.restaurant.data.opening.OpeningHours;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
import es.nami.booking.restaurant.dto.AvailableSlots;
import es.nami.booking.restaurant.dto.DayOfMonthOpening;
import es.nami.booking.restaurant.util.DatesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlotsAvailabilitiesService {

    private final RestaurantDataService restaurantDataService;
    private final OpeningDataService openingDataService;

    public List<AvailableSlots> getSlotsAvailabilitiesForDay(DayOfMonthOpening day, long restaurantId) {
        if (!day.isOpen() || day.isPassed()) {
            return new ArrayList<>();
        }

        Restaurant restaurant = restaurantDataService.findRestaurantById(restaurantId);
        BookingSettings bookingSettings = restaurantDataService.findSettingsByRestaurant(restaurant);
        if (day.isWithSpecialOpeningHours()) {
            return getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);
        } else {
            return getSlotsAvailabilitiesWithUsualOpeningHours(day, restaurant, bookingSettings);
        }
    }

    public List<AvailableSlots> getSlotsAvailabilitiesWithSpecialOpeningHours(DayOfMonthOpening day, Restaurant restaurant, BookingSettings bookingSettings) {
        List<AvailableSlots> availableSlots = new ArrayList<>();
        List<SpecialOpeningHours> specialOpeningHoursList =
                openingDataService.findSpecialOpeningHoursByIds(day.getSpecialOpeningHoursId());
        for (SpecialOpeningHours openingHours : specialOpeningHoursList) {
            LocalDateTime start = openingHours.getStartDateTime();
            LocalDateTime end = start.plus(openingHours.getDuration());
            List<LocalDateTime> bookingTimes = DatesUtil.generateStartDateTimesWithInterval(start, end, bookingSettings.getDuration());
            for (LocalDateTime bookingTime : bookingTimes) {
                // TODO: check bookings and capacity with restaurant infos
                availableSlots.add(AvailableSlots.builder()
                        .startDateTime(bookingTime)
                        .isAvailable(true)
                        .build());
            }
        }
        return availableSlots;
    }

    public List<AvailableSlots> getSlotsAvailabilitiesWithUsualOpeningHours(DayOfMonthOpening day, Restaurant restaurant, BookingSettings bookingSettings) {
        List<AvailableSlots> availableSlots = new ArrayList<>();
        List<OpeningHours> openingHoursList =
                openingDataService.findOpeningHoursByRestaurantAndDayOfWeek(restaurant, day.getDate().getDayOfWeek());
        for (OpeningHours openingHours : openingHoursList) {
            LocalDateTime start = openingHours.getStartTime().atDate(LocalDate.now());
            LocalDateTime end = start.plus(openingHours.getDuration());
            List<LocalDateTime> bookingTimes = DatesUtil.generateStartDateTimesWithInterval(start, end, bookingSettings.getDuration());
            for (LocalDateTime bookingTime : bookingTimes) {
                // TODO: check bookings and capacity
                availableSlots.add(AvailableSlots.builder()
                        .startDateTime(bookingTime)
                        .isAvailable(true)
                        .build());
            }
        }
        return availableSlots;
    }

}
