package es.nami.booking.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class AvailableSlots {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy_HH-mm")
    private LocalDateTime startDateTime;
    private boolean isAvailable;
    // private int availablePlaces;

}
