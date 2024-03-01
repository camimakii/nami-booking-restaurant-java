package es.nami.booking.restaurant.client.service;

import es.nami.booking.restaurant.auth.data.User;
import es.nami.booking.restaurant.auth.service.UserService;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.data.Role;
import es.nami.booking.restaurant.client.data.UserRestaurantRole;
import es.nami.booking.restaurant.client.data.UserRestaurantRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final UserRestaurantRoleRepository userRestaurantRoleRepository;
    private final UserService userService;
    private final RestaurantService restaurantService;

    public UserRestaurantRole assignRole(String userEmail, Long restaurantId, Role role) {
        // 1. Find according infos and return errors if not found
        User user = userService.findUserByEmail(userEmail);
        Restaurant restaurant = restaurantService.findRestaurantById(restaurantId);

        // 2. Check if mapping user-restaurant-role exists already because it must be unique
        Optional<UserRestaurantRole> roleOptional = findByUserAndRestaurant(user, restaurant);

        // 3. Create a new one or update already existing one with new role
        if (roleOptional.isEmpty()) {
            return createNewRole(user, restaurant, role);
        } else {
            UserRestaurantRole userRestaurantRole = roleOptional.get();

            // 4. We avoid useless call to DB
            if (userRestaurantRole.getRole() == role) {
                return userRestaurantRole;
            }

            return updateRole(userRestaurantRole, role);
        }
    }

    private UserRestaurantRole createNewRole(User user, Restaurant restaurant, Role role) {
        return userRestaurantRoleRepository.save(
                UserRestaurantRole.builder()
                        .role(role)
                        .user(user)
                        .restaurant(restaurant)
                        .build());
    }

    private Optional<UserRestaurantRole> findByUserAndRestaurant(User user, Restaurant restaurant) {
        List<UserRestaurantRole> foundRoles =
                userRestaurantRoleRepository.findAllByUserAndRestaurant(
                        user,
                        restaurant);
        if (foundRoles.isEmpty()) {
            return Optional.empty();
        }

        // A mapping user-restaurant should be unique, a user can have only one role in a restaurant
        if (foundRoles.size() > 1) {
            log.error("More than 1 mapping User {} + Restaurant {} found in DB -> shouldn't happen");
        }

        return Optional.of(foundRoles.get(0));
    }

    private UserRestaurantRole updateRole(UserRestaurantRole userRestaurantRole, Role role) {
        userRestaurantRole.setRole(role);
        return userRestaurantRoleRepository.save(userRestaurantRole);
    }

}
