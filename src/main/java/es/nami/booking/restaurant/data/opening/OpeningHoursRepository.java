package es.nami.booking.restaurant.data.opening;

import es.nami.booking.restaurant.data.restaurant.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface OpeningHoursRepository extends CrudRepository<OpeningHours, Long> {

    List<OpeningHours> findAllByRestaurant(Restaurant restaurant);

    List<OpeningHours> findAllByRestaurantAndDayOfWeek(Restaurant restaurant, DayOfWeek dayOfWeek);

}