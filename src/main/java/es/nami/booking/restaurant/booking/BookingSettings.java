package es.nami.booking.restaurant.booking;

import es.nami.booking.restaurant.client.data.Restaurant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;

@Entity
@Table(name = "booking_settings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private int defaultLengthOfBookingInMinutes;

    public Duration getDuration() {
        return Duration.ofMinutes(this.defaultLengthOfBookingInMinutes);
    }

}
