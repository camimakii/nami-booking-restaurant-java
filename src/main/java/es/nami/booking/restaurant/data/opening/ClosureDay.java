package es.nami.booking.restaurant.data.opening;


import com.fasterxml.jackson.annotation.JsonFormat;
import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.util.JsonUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "closure_days")
@Getter
@Setter
public class ClosureDay {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}
