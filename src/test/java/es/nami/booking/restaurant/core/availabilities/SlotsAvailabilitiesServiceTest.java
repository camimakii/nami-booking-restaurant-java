package es.nami.booking.restaurant.core.availabilities;

import es.nami.booking.restaurant.core.data.OpeningHoursDataService;
import es.nami.booking.restaurant.core.data.RestaurantDataService;
import es.nami.booking.restaurant.core.data.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.data.booking.BookingSettings;
import es.nami.booking.restaurant.data.opening.OpeningHours;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.dto.AvailableSlots;
import es.nami.booking.restaurant.dto.DayOfMonth;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SlotsAvailabilitiesServiceTest {

    private static final int MONTH = 2;
    private static final int YEAR = 2020;
    private static final int MODIFIED_HOURS_DAY = 20;
    private static final long MODIFIED_HOURS_ID = 2020220;
    private static final int CLOSED_FULL_DAY = 25;
    private static final long CLOSED_FULL_ID = 2020225;

    @Autowired
    private SlotsAvailabilitiesService slotsAvailabilitiesService;
    @MockBean
    private RestaurantDataService restaurantDataService;
    @MockBean
    private OpeningHoursDataService openingHoursDataService;
    @MockBean
    private SpecialOpeningHoursDataService specialOpeningHoursDataService;

    private Restaurant restaurant;
    private BookingSettings bookingSettings;
    private OpeningHours ohTuesdayMorningOpen;
    private OpeningHours ohTuesdayEveningOpen;
    private OpeningHours ohThursdayEvening;
    private SpecialOpeningHours specialThursday20Feb;
    private SpecialOpeningHours specialTuesday25Feb;

    private void setupMocks() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        when(restaurantDataService.findRestaurantById(eq(restaurant.getId()))).thenReturn(restaurant);

        bookingSettings = new BookingSettings();
        bookingSettings.setDefaultLengthOfBookingInMinutes(2 * 60); // 2 hours dinner
        when(restaurantDataService.findSettingsByRestaurant(eq(restaurant))).thenReturn(bookingSettings);

        // MONDAY is closed the entire day

        // TUESDAY morning is opened from 8:00 to 12:00
        ohTuesdayMorningOpen = new OpeningHours();
        ohTuesdayMorningOpen.setRestaurant(restaurant);
        ohTuesdayMorningOpen.setDayOfWeek(DayOfWeek.TUESDAY);
        ohTuesdayMorningOpen.setOpen(true);
        ohTuesdayMorningOpen.setStartTime(LocalTime.of(8, 00));
        ohTuesdayMorningOpen.setDurationInMinutes(4 * 60); // 4 hours

        // TUESDAY evening is opened from 20:30 to 01:30 of the next day
        ohTuesdayEveningOpen = new OpeningHours();
        ohTuesdayEveningOpen.setRestaurant(restaurant);
        ohTuesdayEveningOpen.setDayOfWeek(DayOfWeek.TUESDAY);
        ohTuesdayEveningOpen.setOpen(true);
        ohTuesdayEveningOpen.setStartTime(LocalTime.of(20, 30));
        ohTuesdayEveningOpen.setDurationInMinutes(5 * 60); // 5 hours
        List<OpeningHours> tuesdayList = Arrays.asList(ohTuesdayMorningOpen, ohTuesdayEveningOpen);

        // WEDNESDAY is closed the entire day

        // THURSDAY is only open during the evening from 19:30 to 01:30 of the next day
        ohThursdayEvening = new OpeningHours();
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

//        // Tuesday 11/2/2020 closed the morning but open in the evening
//        SpecialOpeningHours specialTuesday11Feb = new SpecialOpeningHours();
//        specialTuesday11Feb.setId(CLOSED_MORNING_ID);
//        specialTuesday11Feb.setOpen(false);
//        specialTuesday11Feb.setStartDateTime(LocalDateTime.of(YEAR, MONTH, CLOSED_MORNING_DAY, 7, 0));
//        specialTuesday11Feb.setDurationInMinutes(11 * 60);

        // Thursday 20/2/2020 it will stay open the whole night until 7:00 next morning but starts later
        specialThursday20Feb = new SpecialOpeningHours();
        specialThursday20Feb.setId(MODIFIED_HOURS_ID);
        specialThursday20Feb.setOpen(true);
        specialThursday20Feb.setStartDateTime(LocalDateTime.of(YEAR, MONTH, MODIFIED_HOURS_DAY, 21, 00));
        specialThursday20Feb.setDurationInMinutes(10 * 60);
//
        // Tuesday 25/2/2020 closed whole day
        specialTuesday25Feb = new SpecialOpeningHours();
        specialTuesday25Feb.setId(CLOSED_FULL_ID);
        specialTuesday25Feb.setOpen(false);
        specialTuesday25Feb.setStartDateTime(LocalDateTime.of(YEAR, MONTH, CLOSED_FULL_DAY, 0, 0));
        specialTuesday25Feb.setDurationInMinutes(24 * 60);
//
//        when(specialOpeningHoursDataService
//                .findSpecialOpeningHoursForADate(restaurant, LocalDate.of(YEAR, MONTH, CLOSED_MORNING_DAY)))
//                .thenReturn(Arrays.asList(specialTuesday11Feb));
        when(specialOpeningHoursDataService
                .findSpecialOpeningHoursForADate(restaurant, LocalDate.of(YEAR, MONTH, MODIFIED_HOURS_DAY)))
                .thenReturn(Arrays.asList(specialThursday20Feb));
        when(specialOpeningHoursDataService
                .findSpecialOpeningHoursForADate(restaurant, LocalDate.of(YEAR, MONTH, CLOSED_FULL_DAY)))
                .thenReturn(Arrays.asList(specialTuesday25Feb));
        when(specialOpeningHoursDataService
                .findSpecialOpeningHoursByIds(Arrays.asList(MODIFIED_HOURS_ID)))
                .thenReturn(Arrays.asList(specialThursday20Feb));
        when(specialOpeningHoursDataService
                .findSpecialOpeningHoursByIds(Arrays.asList(CLOSED_FULL_ID)))
                .thenReturn(Arrays.asList(specialTuesday25Feb));
    }

    @Test
    @DisplayName("Day is passed so no slots")
    public void dayIsPassed() {
        setupMocks();
        DayOfMonth day = DayOfMonth.builder()
                .isPassed(true)
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, restaurant.getId());
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Day is closed so no slots")
    public void dayIsClosed() {
        setupMocks();
        DayOfMonth day = DayOfMonth.builder()
                .isOpen(false)
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, restaurant.getId());
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Day is open with 2 usual OpeningHours")
    public void dayIsTuesdayUsual() {
        setupMocks();
        DayOfMonth day = DayOfMonth.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(false)
                .date(LocalDate.of(YEAR, MONTH, 4))
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, restaurant.getId());

        // First slot is correctly part of first opening
        AvailableSlots as1 = result.get(0);
        LocalDateTime startDateTime1 = LocalDateTime.of(day.getDate(), ohTuesdayMorningOpen.getStartTime());
        assertTrue(as1.getStartDateTime().isEqual(startDateTime1));

        // Last slot + duration of a dinner from settings can't overlap the closure hour
        AvailableSlots as2 = result.get(result.size() - 1);
        LocalDateTime startDateTime2 = LocalDateTime.of(day.getDate(), ohTuesdayEveningOpen.getStartTime());
        LocalDateTime closureDateTime = startDateTime2.plus(ohTuesdayEveningOpen.getDuration());
        assertTrue(as2.getStartDateTime().plus(bookingSettings.getDuration()).isBefore(closureDateTime)
                || as2.getStartDateTime().plus(bookingSettings.getDuration()).isEqual(closureDateTime));

        // There's no slot in the close timing between the 2 openings
        LocalDateTime startIntermediaryClose = LocalDateTime.of(day.getDate(), ohTuesdayMorningOpen.getStartTime().plus(ohTuesdayMorningOpen.getDuration()));
        LocalDateTime endIntermediaryClose = LocalDateTime.of(day.getDate(), ohTuesdayEveningOpen.getStartTime());

        for (AvailableSlots as : result) {
            // Either the whole duration of the dinner is before the closure time or the start is after
            assertTrue(
                    as.getStartDateTime().plus(bookingSettings.getDuration()).isBefore(startIntermediaryClose)
                            || as.getStartDateTime().plus(bookingSettings.getDuration()).isEqual(startIntermediaryClose)
                            || as.getStartDateTime().isAfter(endIntermediaryClose)
                            || as.getStartDateTime().isEqual(endIntermediaryClose)
            );
        }
    }

    @Test
    @DisplayName("Day is open with 1 usual OpeningHours")
    public void dayIsThursdayUsual() {
        setupMocks();
        DayOfMonth day = DayOfMonth.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(false)
                .date(LocalDate.of(YEAR, MONTH, 6))
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, restaurant.getId());

        // First slot is correctly part of opening
        AvailableSlots as1 = result.get(0);
        LocalDateTime startDateTime = LocalDateTime.of(day.getDate(), ohThursdayEvening.getStartTime());
        assertTrue(as1.getStartDateTime().isEqual(startDateTime));

        // Last slot + duration of a dinner from settings can't overlap the closure hour
        AvailableSlots as2 = result.get(result.size() - 1);
        LocalDateTime closureDateTime = startDateTime.plus(ohThursdayEvening.getDuration());
        assertTrue(as2.getStartDateTime().plus(bookingSettings.getDuration()).isBefore(closureDateTime)
                || as2.getStartDateTime().plus(bookingSettings.getDuration()).isEqual(closureDateTime));
    }

    @Test
    @DisplayName("Day is open with 1 special OpeningHours")
    public void dayIsThursdaySpecial() {
        setupMocks();
        DayOfMonth day = DayOfMonth.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(true)
                .date(LocalDate.of(YEAR, MONTH, MODIFIED_HOURS_DAY))
                .specialOpeningHoursId(Arrays.asList(MODIFIED_HOURS_ID))
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, restaurant.getId());

        result.forEach(r -> System.out.println(r));

        // First slot is correctly part of opening
        AvailableSlots as1 = result.get(0);
        LocalDateTime startDateTime = specialThursday20Feb.getStartDateTime();
        assertTrue(as1.getStartDateTime().isEqual(startDateTime));

        // Last slot + duration of a dinner from settings can't overlap the closure hour
        AvailableSlots as2 = result.get(result.size() - 1);
        LocalDateTime closureDateTime = startDateTime.plus(specialThursday20Feb.getDuration());
        assertTrue(as2.getStartDateTime().plus(bookingSettings.getDuration()).isBefore(closureDateTime)
                || as2.getStartDateTime().plus(bookingSettings.getDuration()).isEqual(closureDateTime));
    }

}