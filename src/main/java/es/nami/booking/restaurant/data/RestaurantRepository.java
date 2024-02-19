package es.nami.booking.restaurant.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {

    List<Restaurant> findAllByRestaurantGroup(RestaurantGroup restaurantGroup);

}