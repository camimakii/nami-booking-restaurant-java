package es.nami.booking.restaurant.client.data;

import es.nami.booking.restaurant.auth.data.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRestaurantRoleRepository extends CrudRepository<UserRestaurantRole, Long> {

    List<UserRestaurantRole> findAllByUserAndRestaurant(User user, Restaurant restaurant);

}
