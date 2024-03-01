package es.nami.booking.restaurant.client.data;

import es.nami.booking.restaurant.annotation.JsonToString;
import es.nami.booking.restaurant.util.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "restaurant_group")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonToString
public class RestaurantGroup {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Override
    public boolean equals(Object o) {
        return this.toString().equals(o.toString());
    }

}
