package es.nami.booking.restaurant.api.availabilities;

import es.nami.booking.restaurant.core.availabilities.DaysAvailabilitiesService;
import es.nami.booking.restaurant.core.availabilities.SlotsAvailabilitiesService;
import es.nami.booking.restaurant.dto.AvailableSlots;
import es.nami.booking.restaurant.dto.DayOfMonth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
@Slf4j
@RequiredArgsConstructor
public class AvailabilitiesRestController {

    private final DaysAvailabilitiesService daysAvailabilitiesService;
    private final SlotsAvailabilitiesService slotsAvailabilitiesService;

    @GetMapping(value = "/days-of-month")
    public ResponseEntity<List<DayOfMonth>> getDaysAvailabilitiesForMonth(
            @RequestParam long restaurantId,
            @RequestParam int month,
            @RequestParam int year) {
        return new ResponseEntity<>(
                daysAvailabilitiesService.getDaysAvailabilitiesOfMonthForRestaurant(restaurantId, month, year),
                HttpStatus.OK);
    }

    @PostMapping(value = "/slots-of-day") // using POST for read with JSON
    public ResponseEntity<List<AvailableSlots>> getSlotsAvailabilitiesForDay(
            @RequestParam long restaurantId,
            @RequestBody DayOfMonth day) { // TODO: Add Validation
        return new ResponseEntity<>(
                slotsAvailabilitiesService.getSlotsAvailabilitiesForDay(day, restaurantId),
                HttpStatus.OK);
    }

}