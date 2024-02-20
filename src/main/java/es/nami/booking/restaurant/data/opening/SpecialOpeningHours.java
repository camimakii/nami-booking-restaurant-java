package es.nami.booking.restaurant.data.opening;

import es.nami.booking.restaurant.data.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "exceptional_business_hours")
@Getter
public class SpecialOpeningHours {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Setter
    private Restaurant restaurant;

    @Column(nullable = false)
    @Setter
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    @Setter
    private long durationInMinutes;

    public Duration getDuration() {
        return Duration.ofMinutes(this.durationInMinutes);
    }

}
