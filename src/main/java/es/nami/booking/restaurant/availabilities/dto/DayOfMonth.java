package es.nami.booking.restaurant.availabilities.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.nami.booking.restaurant.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DayOfMonth {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    private boolean isOpen;

    private boolean isPassed;

    private boolean isWithSpecialOpeningHours;

    private List<Long> specialOpeningHoursId;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

}
