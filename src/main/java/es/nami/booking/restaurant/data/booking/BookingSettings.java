package es.nami.booking.restaurant.data.booking;

import es.nami.booking.restaurant.data.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Entity
@Table(name = "booking_settings")
@Getter
public class BookingSettings {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @Setter
    private Restaurant restaurant;

//    @Deprecated
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    @Setter
//    private BookingTimingType bookingTimingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private BookingCapacityType bookingCapacityType;

    @Column(nullable = false)
    @Setter
    private long defaultLengthOfBookingInMinutes;

    public Duration getDuration() {
        return Duration.ofMinutes(this.defaultLengthOfBookingInMinutes);
    }

}
