package es.nami.booking.restaurant.core;

import es.nami.booking.restaurant.data.RestaurantGroup;
import es.nami.booking.restaurant.data.RestaurantGroupRepository;
import es.nami.booking.restaurant.exception.NamiException;
import es.nami.booking.restaurant.util.StreamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantGroupDataService {

    private final RestaurantGroupRepository restaurantGroupRepository;

    public RestaurantGroup findRestaurantGroupById(long restaurantGroupId) {
        Optional<RestaurantGroup> groupOptional = restaurantGroupRepository.findById(restaurantGroupId);
        return NamiException.ifNotFound(groupOptional, "RestaurantGroup", restaurantGroupId);
    }

    public List<RestaurantGroup> findAllRestaurantGroups() {
        return StreamUtil.transformIterableToList(restaurantGroupRepository.findAll());
    }

}
