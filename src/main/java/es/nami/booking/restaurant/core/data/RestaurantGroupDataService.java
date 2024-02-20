package es.nami.booking.restaurant.core.data;

import es.nami.booking.restaurant.data.restaurant.RestaurantGroup;
import es.nami.booking.restaurant.data.restaurant.RestaurantGroupRepository;
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

    public static final String ENTITY_NAME = "RestaurantGroup";

    private final RestaurantGroupRepository restaurantGroupRepository;

    public RestaurantGroup createNewRestaurantGroup(RestaurantGroup restaurantGroup) {
        restaurantGroup.setId(null);
        return restaurantGroupRepository.save(restaurantGroup);
    }

    public RestaurantGroup findRestaurantGroupById(long restaurantGroupId) {
        Optional<RestaurantGroup> groupOptional = restaurantGroupRepository.findById(restaurantGroupId);
        return NamiException.ifNotFound(groupOptional, ENTITY_NAME, restaurantGroupId);
    }

    public List<RestaurantGroup> findAllRestaurantGroups() {
        return StreamUtil.transformIterableToList(restaurantGroupRepository.findAll());
    }

    public RestaurantGroup updateRestaurantGroup(RestaurantGroup restaurantGroup) {
        if (!restaurantGroupRepository.existsById(restaurantGroup.getId())) {
            throw NamiException.notFoundConstructor(ENTITY_NAME, restaurantGroup.getId());
        }
        return restaurantGroupRepository.save(restaurantGroup);
    }

    public void deleteRestaurantGroupAndCascade(Long restaurantGroupId) {
        RestaurantGroup restaurantGroup = findRestaurantGroupById(restaurantGroupId);
        restaurantGroupRepository.delete(restaurantGroup);
    }

}
