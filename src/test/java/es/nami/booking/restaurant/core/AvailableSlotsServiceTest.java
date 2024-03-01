package es.nami.booking.restaurant.core;

import es.nami.booking.restaurant.availabilities.service.SlotsAvailabilitiesService;
import es.nami.booking.restaurant.opening.service.OpeningHoursDataService;
import es.nami.booking.restaurant.client.service.RestaurantService;
import es.nami.booking.restaurant.opening.service.SpecialOpeningHoursDataService;
import es.nami.booking.restaurant.booking.BookingSettings;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.availabilities.dto.AvailableSlots;
import es.nami.booking.restaurant.availabilities.dto.DayOfMonth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
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
    private RestaurantService restaurantService;
    @MockBean
    private OpeningHoursDataService openingHoursDataService;
    @MockBean
    private SpecialOpeningHoursDataService specialOpeningHoursDataService;

    @Test
    void getAvailableSlotsForDay_dayIsClosed_returnEmptyList() {
        DayOfMonth day = DayOfMonth.builder()
                .isOpen(false)
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, 1L);
        assertEquals(0, result.size());
    }

    @Test
    void getAvailableSlotsForDay_dayIsPassed_returnEmptyList() {
        DayOfMonth day = DayOfMonth.builder()
                .isPassed(true)
                .build();
        List<AvailableSlots> result = slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, 1L);
        assertEquals(0, result.size());
    }

    @Test
    void getAvailableSlotsForDay_dayIsOpenWithSpecialHours_callsSpecialHours() {
        DayOfMonth day = DayOfMonth.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(true)
                .build();
        Restaurant restaurant = new Restaurant();
        BookingSettings bookingSettings = new BookingSettings();
        SlotsAvailabilitiesService spy = spy(slotsAvailabilitiesService);

        when(restaurantService.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(restaurantService.findSettingsByRestaurant(restaurant)).thenReturn(bookingSettings);
        when(specialOpeningHoursDataService.findSpecialOpeningHoursByIds(anyList())).thenReturn(new ArrayList<>());

        spy.getSlotsAvailabilitiesForDay(day, 1L);

        verify(spy, times(1)).getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);
        verify(spy, times(0)).getSlotsAvailabilitiesWithUsualOpeningHours(day, restaurant, bookingSettings);
    }

    @Test
    void getAvailableSlotsForDay_dayIsOpenWithUsualHours_callsUsualHours() {
        DayOfMonth day = DayOfMonth.builder()
                .isOpen(true)
                .isWithSpecialOpeningHours(false)
                .date(LocalDate.now())
                .build();
        Restaurant restaurant = new Restaurant();
        BookingSettings bookingSettings = new BookingSettings();
        SlotsAvailabilitiesService spy = spy(slotsAvailabilitiesService);

        when(restaurantService.findRestaurantById(anyLong())).thenReturn(restaurant);
        when(restaurantService.findSettingsByRestaurant(eq(restaurant))).thenReturn(bookingSettings);
        when(openingHoursDataService.findOpeningHoursByRestaurantAndDayOfWeek(eq(restaurant), any(DayOfWeek.class))).thenReturn(new ArrayList<>());

        spy.getSlotsAvailabilitiesForDay(day, 1L);

        verify(spy, times(0)).getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);
        verify(spy, times(1)).getSlotsAvailabilitiesWithUsualOpeningHours(day, restaurant, bookingSettings);
    }

////    @Test
//    // TODO
//    void getAvailableSlotsWithSpecialOpeningHours_finds2SpecialOpeningHours_datesUtilsCalls2Times() {
////        MockitoAnnotations.initMocks(this);
//      //  MockedStatic<DatesUtil> mockedStatic = mockStatic(DatesUtil.class);
//
////        mockedStatic.when(DatesUtil::generateStartDateTimesWithInterval).thenReturn("Expected Value");
//
//        DayOfMonth day = DayOfMonth.builder()
//                .isOpen(true)
//                .isWithSpecialOpeningHours(false)
//                .date(LocalDate.now())
//                .specialOpeningHoursId(Arrays.asList(1L, 2L))
//                .build();
//        Restaurant restaurant = new Restaurant();
//        BookingSettings bookingSettings = new BookingSettings();
//        SpecialOpeningHours soh1 = new SpecialOpeningHours();
//        SpecialOpeningHours soh2 = new SpecialOpeningHours();
//
//        //mockedStatic.when(() -> DatesUtil.generateStartDateTimesWithInterval(any(LocalDateTime.class), any(LocalDateTime.class), any(Duration.class))).thenReturn(Arrays.asList(LocalDateTime.now(), LocalDateTime.now().plusHours(1)));
//        when(restaurantDataService.findRestaurantById(anyLong())).thenReturn(restaurant);
//        when(restaurantDataService.findSettingsByRestaurant(eq(restaurant))).thenReturn(bookingSettings);
//        when(openingHoursDataService.findSpecialOpeningHoursByIds(eq(day.getSpecialOpeningHoursId()))).thenThrow(new RuntimeException());
//
//        List<AvailableSlots> slots = slotsAvailabilitiesService.getSlotsAvailabilitiesWithSpecialOpeningHours(day, restaurant, bookingSettings);
//
//       // mockedStatic.verify(() -> DatesUtil.generateStartDateTimesWithInterval(any(LocalDateTime.class), any(LocalDateTime.class), any(Duration.class)), times(2));
//        assertEquals(4, slots.size());
//    }
}