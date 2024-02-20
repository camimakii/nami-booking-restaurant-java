package es.nami.booking.restaurant.core.data;

import es.nami.booking.restaurant.data.restaurant.Restaurant;
import es.nami.booking.restaurant.data.opening.ClosureDay;
import es.nami.booking.restaurant.data.opening.ClosureDayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClosureDayDataService {

    private final ClosureDayRepository closureDayRepository;

    public Optional<ClosureDay> findClosureDayForRestaurantAndDate(Restaurant restaurant, LocalDate date) {
        return closureDayRepository.findOneByRestaurantAndDate(restaurant, date);
    }

    public ClosureDay createOneClosureDay(ClosureDay closureDay) {
        return closureDayRepository.save(closureDay);
    }

    public boolean deleteOneClosureDay(long closureDayId) {
        closureDayRepository.deleteById(closureDayId);
        return true;
    }

}
