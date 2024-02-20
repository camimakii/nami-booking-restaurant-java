package es.nami.booking.restaurant.data.opening;

import es.nami.booking.restaurant.data.restaurant.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ClosureDayRepository extends CrudRepository<ClosureDay, Long> {

    Optional<ClosureDay> findOneByRestaurantAndDate(Restaurant restaurant, LocalDate date);

}