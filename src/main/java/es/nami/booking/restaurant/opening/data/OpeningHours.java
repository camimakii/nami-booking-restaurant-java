package es.nami.booking.restaurant.opening.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.util.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;

@Entity
@Table(name = "opening_hours")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHours {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;

    @Column(nullable = false)
    private int durationInMinutes;

    @JsonIgnore
    @Transient
    public Duration getDuration() {
        return Duration.ofMinutes(this.durationInMinutes);
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}
