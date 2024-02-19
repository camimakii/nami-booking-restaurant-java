package es.nami.booking.restaurant.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "restaurant_group")
@Getter
public class RestaurantGroup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @Setter
    private String name;

}
