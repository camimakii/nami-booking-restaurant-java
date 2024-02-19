package es.nami.booking.restaurant.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restaurant")
@Getter
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "restaurant_group_id", nullable = false)
    @Setter
    @JsonIgnore
    private RestaurantGroup restaurantGroup;

}
