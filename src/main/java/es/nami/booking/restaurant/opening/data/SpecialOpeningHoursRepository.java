package es.nami.booking.restaurant.opening.data;

import es.nami.booking.restaurant.client.data.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SpecialOpeningHoursRepository extends CrudRepository<SpecialOpeningHours, Long> {

    List<SpecialOpeningHours> findAllByRestaurant(Restaurant restaurant);
    List<SpecialOpeningHours> findAllByRestaurantAndStartDateTimeBetween(Restaurant restaurant, LocalDateTime start, LocalDateTime end);

}