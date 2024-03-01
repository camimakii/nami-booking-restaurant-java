package es.nami.booking.restaurant.availabilities.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.nami.booking.restaurant.availabilities.service.DaysAvailabilitiesService;
import es.nami.booking.restaurant.availabilities.service.SlotsAvailabilitiesService;
import es.nami.booking.restaurant.availabilities.dto.AvailableSlots;
import es.nami.booking.restaurant.availabilities.dto.DayOfMonth;
import es.nami.booking.restaurant.util.Constants;
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

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(Constants.API_URL + "availabilities")
@Slf4j
@RequiredArgsConstructor
public class AvailabilitiesRestController {

    private final DaysAvailabilitiesService daysAvailabilitiesService;
    private final SlotsAvailabilitiesService slotsAvailabilitiesService;

    @GetMapping(value = "/days-of-month")
    public ResponseEntity<List<DayOfMonth>> getDaysAvailabilitiesForMonth(
            @RequestParam long restaurantId,
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") LocalDate today) {
        return new ResponseEntity<>(
                daysAvailabilitiesService.getDaysAvailabilitiesOfMonthForRestaurant(restaurantId, month, year, today),
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
