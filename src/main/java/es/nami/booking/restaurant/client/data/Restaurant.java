package es.nami.booking.restaurant.client.data;

import es.nami.booking.restaurant.annotation.JsonToString;
import es.nami.booking.restaurant.util.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonToString
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "restaurant_group_id", nullable = false)
    private RestaurantGroup restaurantGroup;

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}
