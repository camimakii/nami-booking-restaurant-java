package es.nami.booking.restaurant.data.opening;


import es.nami.booking.restaurant.data.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "closure_days")
@Getter
public class ClosureDay {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Setter
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Setter
    private Restaurant restaurant;

}
