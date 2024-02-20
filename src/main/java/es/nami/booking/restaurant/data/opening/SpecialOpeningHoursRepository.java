package es.nami.booking.restaurant.data.opening;

import es.nami.booking.restaurant.data.restaurant.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SpecialOpeningHoursRepository extends CrudRepository<SpecialOpeningHours, Long> {

    List<SpecialOpeningHours> findAllByRestaurantAndStartDateTimeBetween(Restaurant restaurant, LocalDateTime start, LocalDateTime end);

}