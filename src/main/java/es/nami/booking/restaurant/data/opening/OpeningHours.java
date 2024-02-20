package es.nami.booking.restaurant.data.opening;

import es.nami.booking.restaurant.data.restaurant.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

@Entity
@Table(name = "opening_hours")
@Getter
public class OpeningHours {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Setter
    private Restaurant restaurant;

    @Column
    @Setter
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    @Setter
    private LocalTime startTime;

    @Column(nullable = false)
    @Setter
    private long durationInMinutes;

    public Duration getDuration() {
        return Duration.ofMinutes(this.durationInMinutes);
    }

}
