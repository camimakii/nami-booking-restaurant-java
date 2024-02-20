package es.nami.booking.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
public class DayOfMonth {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    private boolean isOpen;

    private boolean isPassed;

    private boolean isWithSpecialOpeningHours;

    private List<Long> specialOpeningHoursId;

}