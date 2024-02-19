package es.nami.booking.restaurant.core;

import es.nami.booking.restaurant.data.Restaurant;
import es.nami.booking.restaurant.data.booking.BookingSettings;
import es.nami.booking.restaurant.data.opening.SpecialOpeningHours;
import es.nami.booking.restaurant.dto.AvailableSlots;
import es.nami.booking.restaurant.dto.DayOfMonthOpening;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AvailableSlotsServiceTest {

    @Autowired
    private SlotsAvailabilitiesService slotsAvailabilitiesService;
    @MockBean
    private RestaurantDataService restaurantDataService;
    @MockBean
    private OpeningDataService openingDataService;

    @Test
    void getAvailableSlotsForDay_dayIsClosed_returnEmptyList() {
        DayOfMonthOpening day = DayOfMonthOpening.builder()
                .isOpen(false)
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, 1L);
        assertEquals(0, result.size());
    }

    @Test
    void getAvailableSlotsForDay_dayIsPassed_returnEmptyList() {
        DayOfMonthOpening day = DayOfMonthOpening.builder()
                .isPassed(true)
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, 1L);
        assertEquals(0, result.size());
    }

    @Test
    void getAvailableSlotsForDay_dayIsOpenWithSpecialHours_callsSpecialHours() {
        DayOfMonthOpening day = DayOfMonthOpening.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(true)
                .build();
        Restaurant restaurant = new Restaurant();
        BookingSettings bookingSettings = new BookingSettings();
        SlotsAvailabilitiesService spy = spy(slotsAvailabilitiesService);

        when(restaurantDataService.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(restaurantDataService.findSettingsByRestaurant(restaurant)).thenReturn(bookingSettings);
        when(openingDataService.findSpecialOpeningHoursByIds(anyList())).thenReturn(new ArrayList<>());

        spy.getSlotsAvailabilitiesForDay(day, 1L);

        verify(spy, times(1)).getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);
        verify(spy, times(0)).getSlotsAvailabilitiesWithUsualOpeningHours(day, restaurant, bookingSettings);
    }

    @Test
    void getAvailableSlotsForDay_dayIsOpenWithUsualHours_callsUsualHours() {
        DayOfMonthOpening day = DayOfMonthOpening.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(false)
                .date(LocalDate.now())
                .build();
        Restaurant restaurant = new Restaurant();
        BookingSettings bookingSettings = new BookingSettings();
        SlotsAvailabilitiesService spy = spy(slotsAvailabilitiesService);

        when(restaurantDataService.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(restaurantDataService.findSettingsByRestaurant(eq(restaurant))).thenReturn(bookingSettings);
        when(openingDataService.findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), any(DayOfWeek.class))).thenReturn(new ArrayList<>());

        spy.getSlotsAvailabilitiesForDay(day, 1L);

        verify(spy, times(0)).getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);
        verify(spy, times(1)).getSlotsAvailabilitiesWithUsualOpeningHours(day, restaurant, bookingSettings);
    }

//    @Test
    // TODO
    void getAvailableSlotsWithSpecialOpeningHours_finds2SpecialOpeningHours_datesUtilsCalls2Times() {
//        MockitoAnnotations.initMocks(this);
      //  MockedStatic<DatesUtil> mockedStatic = mockStatic(DatesUtil.class);

//        mockedStatic.when(DatesUtil::generateStartDateTimesWithInterval).thenReturn("Expected Value");

        DayOfMonthOpening day = DayOfMonthOpening.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(false)
                .date(LocalDate.now())
                .specialOpeningHoursId(Arrays.asList(1L, 2L))
                .build();
        Restaurant restaurant = new Restaurant();
        BookingSettings bookingSettings = new BookingSettings();
        SpecialOpeningHours soh1 = new SpecialOpeningHours();
        SpecialOpeningHours soh2 = new SpecialOpeningHours();

        //mockedStatic.when(() -> DatesUtil.generateStartDateTimesWithInterval(any(LocalDateTime.class), any(LocalDateTime.class), any(Duration.class))).thenReturn(Arrays.asList(LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
        when(restaurantDataService.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(restaurantDataService.findSettingsByRestaurant(eq(restaurant))).thenReturn(bookingSettings);
        when(openingDataService.findSpecialOpeningHoursByIds(eq(day.getSpecialOpeningHoursId()))).thenThrow(new RuntimeException());

        List<AvailableSlots> slots = slotsAvailabilitiesService.getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);

       // mockedStatic.verify(() -> DatesUtil.generateStartDateTimesWithInterval(any(LocalDateTime.class), any(LocalDateTime.class), any(Duration.class)), times(2));
        assertEquals(4, slots.size());
    }
}