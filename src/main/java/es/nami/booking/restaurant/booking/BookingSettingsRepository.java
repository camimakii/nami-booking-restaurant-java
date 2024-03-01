package es.nami.booking.restaurant.booking;

import es.nami.booking.restaurant.client.data.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookingSettingsRepository extends CrudRepository<BookingSettings, Long> {

    Optional<BookingSettings> findOneByRestaurant(Restaurant restaurant);

}