package es.nami.booking.restaurant.availabilities.service;

import es.nami.booking.restaurant.availabilities.dto.AvailableSlots;
import es.nami.booking.restaurant.availabilities.dto.DayOfMonth;
import es.nami.booking.restaurant.booking.BookingSettings;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.service.RestaurantService;
import es.nami.booking.restaurant.opening.data.OpeningHours;
import es.nami.booking.restaurant.opening.data.SpecialOpeningHours;
import es.nami.booking.restaurant.opening.service.OpeningHoursDataService;
import es.nami.booking.restaurant.opening.service.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.util.DatesUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlotsAvailabilitiesService {

    private final RestaurantService restaurantService;
    private final OpeningHoursDataService openingHoursDataService;
    private final SpecialOpeningHoursDataService specialOpeningHoursDataService;

    public List<AvailableSlots> getSlotsAvailabilitiesForDay(DayOfMonth day, long restaurantId) {
        if (!day.isOpen() || day.isPassed()) {
            return new ArrayList<>();
        }

        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);
        BookingSettings bookingSettings = restaurantService.findSettingsByRestaurant(restaurant);
        if (day.isWithSpecialOpeningHours()) {
            return getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);
        } else {
            return getSlotsAvailabilitiesWithUsualOpeningHours(day, restaurant, bookingSettings);
        }
    }

    public List<AvailableSlots> getSlotsAvailabilitiesWithUsualOpeningHours(DayOfMonth day, Restaurant restaurant, BookingSettings bookingSettings) {
        List<AvailableSlots> availableSlots = new ArrayList<>();
        List<OpeningHours> openingHoursList =
                openingHoursDataService.findOpeningHoursByRestaurantAndDayOfWeek(restaurant, day.getDate().getDayOfWeek());
        for (OpeningHours openingHours : openingHoursList) {
            LocalDateTime start = openingHours.getStartTime().atDate(day.getDate());
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

    public List<AvailableSlots> getSlotsAvailabilitiesWithSpecialOpeningHours(DayOfMonth day, Restaurant restaurant, BookingSettings bookingSettings) {
        List<AvailableSlots> availableSlots = new ArrayList<>();
        List<SpecialOpeningHours> specialOpeningHoursList =
                specialOpeningHoursDataService.findSpecialOpeningHoursByIds(day.getSpecialOpeningHoursId());
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

}
