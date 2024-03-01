package es.nami.booking.restaurant.core.availabilities;

import es.nami.booking.restaurant.availabilities.service.DaysAvailabilitiesService;
import es.nami.booking.restaurant.opening.service.OpeningHoursDataService;
import es.nami.booking.restaurant.client.service.RestaurantService;
import es.nami.booking.restaurant.opening.service.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.opening.data.OpeningHours;
import es.nami.booking.restaurant.opening.data.SpecialOpeningHours;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.availabilities.dto.DayOfMonth;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class DaysAvailabilitiesServiceTest {

    private static final int TODAY = 6;
    private static final int MONTH = 2;
    private static final int YEAR = 2020;
    private static final int CLOSED_MORNING_DAY = 11;
    private static final long CLOSED_MORNING_ID = 2020211;
    private static final int MODIFIED_HOURS_DAY = 20;
    private static final long MODIFIED_HOURS_ID = 2020220;
    private static final int CLOSED_FULL_DAY = 25;
    private static final long CLOSED_FULL_ID = 2020225;

    @Autowired
    private DaysAvailabilitiesService daysAvailabilitiesService;
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private OpeningHoursDataService openingHoursDataService;
    @MockBean
    private SpecialOpeningHoursDataService specialOpeningHoursDataService;

    private Restaurant restaurant;
    private LocalDate today;

    private void setupMocks() {
        today = LocalDate.of(YEAR, MONTH, TODAY);
        restaurant = new Restaurant();
        restaurant.setId(1L);
        when(restaurantService.findRestaurantById(eq(restaurant.getId()))).thenReturn(restaurant);

        // MONDAY is closed the entire day

        // TUESDAY morning is opened from 8:00 to 12:00
        OpeningHours ohTuesdayMorningOpen = new OpeningHours();
        ohTuesdayMorningOpen.setRestaurant(restaurant);
        ohTuesdayMorningOpen.setDayOfWeek(DayOfWeek.TUESDAY);
        ohTuesdayMorningOpen.setOpen(true);
        ohTuesdayMorningOpen.setStartTime(LocalTime.of(8, 00));
        ohTuesdayMorningOpen.setDurationInMinutes(4 * 60); // 4 hours

        // TUESDAY evening is opened from 20:30 to 01:30 of the next day
        OpeningHours ohTuesdayEveningOpen = new OpeningHours();
        ohTuesdayEveningOpen.setRestaurant(restaurant);
        ohTuesdayEveningOpen.setDayOfWeek(DayOfWeek.TUESDAY);
        ohTuesdayEveningOpen.setOpen(true);
        ohTuesdayEveningOpen.setStartTime(LocalTime.of(20, 30));
        ohTuesdayEveningOpen.setDurationInMinutes(5 * 60); // 5 hours
        List<OpeningHours> tuesdayList = Arrays.asList(ohTuesdayMorningOpen, ohTuesdayEveningOpen);

        // WEDNESDAY is closed the entire day

        // THURSDAY is only open during the evening from 19:30 to 01:30 of the next day
        OpeningHours ohThursdayEvening = new OpeningHours();
        ohThursdayEvening.setRestaurant(restaurant);
        ohThursdayEvening.setDayOfWeek(DayOfWeek.THURSDAY);
        ohThursdayEvening.setOpen(true);
        ohThursdayEvening.setStartTime(LocalTime.of(19, 30));
        ohThursdayEvening.setDurationInMinutes(6 * 60); // 5 hours
        List<OpeningHours> thursdayList = Arrays.asList(ohThursdayEvening);

        when(openingHoursDataService
                .findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), eq(DayOfWeek.MONDAY)))
                .thenReturn(new ArrayList<>());
        when(openingHoursDataService
                .findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), eq(DayOfWeek.TUESDAY)))
                .thenReturn(tuesdayList);
        when(openingHoursDataService
                .findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), eq(DayOfWeek.WEDNESDAY)))
                .thenReturn(new ArrayList<>());
        when(openingHoursDataService
                .findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), eq(DayOfWeek.THURSDAY)))
                .thenReturn(thursdayList);
        when(openingHoursDataService
                .findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), eq(DayOfWeek.FRIDAY)))
                .thenReturn(new ArrayList<>());
        when(openingHoursDataService
                .findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), eq(DayOfWeek.SATURDAY)))
                .thenReturn(new ArrayList<>());
        when(openingHoursDataService
                .findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), eq(DayOfWeek.SUNDAY)))
                .thenReturn(new ArrayList<>());

        // Tuesday 11/2/2020 closed the morning but open in the evening
        SpecialOpeningHours specialTuesday11Feb = new SpecialOpeningHours();
        specialTuesday11Feb.setId(CLOSED_MORNING_ID);
        specialTuesday11Feb.setOpen(false);
        specialTuesday11Feb.setStartDateTime(LocalDateTime.of(YEAR, MONTH, CLOSED_MORNING_DAY, 7, 0));
        specialTuesday11Feb.setDurationInMinutes(11 * 60);

        // Thursday 20/2/2020 it will stay open the whole night until 7:00 next morning but starts later
        SpecialOpeningHours specialThursday20Feb = new SpecialOpeningHours();
        specialThursday20Feb.setId(MODIFIED_HOURS_ID);
        specialThursday20Feb.setOpen(true);
        specialThursday20Feb.setStartDateTime(LocalDateTime.of(YEAR, MONTH, MODIFIED_HOURS_DAY, 21, 00));
        specialThursday20Feb.setDurationInMinutes(10 * 60);

        // Tuesday 25/2/2020 closed whole day
        SpecialOpeningHours specialTuesday25Feb = new SpecialOpeningHours();
        specialTuesday25Feb.setId(CLOSED_FULL_ID);
        specialTuesday25Feb.setOpen(false);
        specialTuesday25Feb.setStartDateTime(LocalDateTime.of(YEAR, MONTH, CLOSED_FULL_DAY, 0, 0));
        specialTuesday25Feb.setDurationInMinutes(24 * 60);

        when(specialOpeningHoursDataService
                .findSpecialOpeningHoursForADate(restaurant, LocalDate.of(YEAR, MONTH, CLOSED_MORNING_DAY)))
                .thenReturn(Arrays.asList(specialTuesday11Feb));
        when(specialOpeningHoursDataService
                .findSpecialOpeningHoursForADate(restaurant, LocalDate.of(YEAR, MONTH, MODIFIED_HOURS_DAY)))
                .thenReturn(Arrays.asList(specialThursday20Feb));
        when(specialOpeningHoursDataService
                .findSpecialOpeningHoursForADate(restaurant, LocalDate.of(YEAR, MONTH, CLOSED_FULL_DAY)))
                .thenReturn(Arrays.asList(specialTuesday25Feb));
    }

    @Test
    @DisplayName("Get days of a month and their availabilities")
    public void getDaysAndTheirAvailabilities() {
        setupMocks();
        List<DayOfMonth> days =
                daysAvailabilitiesService.getDaysAvailabilitiesOfMonthForRestaurant(restaurant.getId(), YEAR, MONTH, today);

        // Correct amount of days -> February 2020 was leap !
        assertEquals(29, days.size());

        for (DayOfMonth day : days) {
            if (day.getDate().isBefore(today)) {
                // Days before "today" should be marked "isPassed = true"
                assertTrue(day.isPassed());
            } else if (day.getDate().isEqual(LocalDate.of(YEAR, MONTH, CLOSED_MORNING_DAY))) {
                // This day should be marked as special and closed because the special hours override the usual hours so not open in the evening
                assertTrue(day.isWithSpecialOpeningHours());
                assertTrue(day.getSpecialOpeningHoursId().contains(CLOSED_MORNING_ID));
                assertFalse(day.isOpen());
                assertFalse(day.isPassed());
            } else if (day.getDate().isEqual(LocalDate.of(YEAR, MONTH, MODIFIED_HOURS_DAY))) {
                // This day should be marked as special because its hours have changed. It's unusual but still open
                assertTrue(day.isWithSpecialOpeningHours());
                assertTrue(day.getSpecialOpeningHoursId().contains(MODIFIED_HOURS_ID));
                assertTrue(day.isOpen());
                assertFalse(day.isPassed());
            } else if (day.getDate().isEqual(LocalDate.of(YEAR, MONTH, CLOSED_FULL_DAY))) {
                // This day should be marked as special because it's exceptional but entirely closed
                assertTrue(day.isWithSpecialOpeningHours());
                assertTrue(day.getSpecialOpeningHoursId().contains(CLOSED_FULL_ID));
                assertFalse(day.isOpen());
                assertFalse(day.isPassed());
            } else if (day.getDate().getDayOfWeek().equals(DayOfWeek.TUESDAY) || day.getDate().getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
                // Usually open on Tuesdays or Thursdays
                assertTrue(day.isOpen());
                assertFalse(day.isWithSpecialOpeningHours());
                assertFalse(day.isPassed());
                assertEquals(0, day.getSpecialOpeningHoursId().size());
            } else {
                // Other days are usually closed
                assertFalse(day.isOpen());
                assertFalse(day.isWithSpecialOpeningHours());
                assertFalse(day.isPassed());
                assertTrue(day.getSpecialOpeningHoursId().isEmpty());
            }
        }
    }


}