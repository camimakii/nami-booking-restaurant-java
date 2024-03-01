package es.nami.booking.restaurant.client.service;

import es.nami.booking.restaurant.auth.data.User;
import es.nami.booking.restaurant.auth.dto.AuthenticationResponse;
import es.nami.booking.restaurant.auth.dto.UserRequest;
import es.nami.booking.restaurant.auth.service.AuthenticationService;
import es.nami.booking.restaurant.auth.service.UserService;
import es.nami.booking.restaurant.client.data.Restaurant;
import es.nami.booking.restaurant.client.data.RestaurantGroup;
import es.nami.booking.restaurant.client.data.RestaurantGroupRepository;
import es.nami.booking.restaurant.client.data.RestaurantRepository;
import es.nami.booking.restaurant.client.data.Role;
import es.nami.booking.restaurant.client.data.UserRestaurantRole;
import es.nami.booking.restaurant.client.data.UserRestaurantRoleRepository;
import es.nami.booking.restaurant.client.dto.CreateRestaurantGroupRequest;
import es.nami.booking.restaurant.client.dto.CreateRestaurantGroupResponse;
import es.nami.booking.restaurant.error.NamiException;
import es.nami.booking.restaurant.util.StreamUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantGroupService {

    public static final String ENTITY_NAME = "RestaurantGroup";

    private final RestaurantGroupRepository restaurantGroupRepository;
    private final RestaurantRepository restaurantRepository; // No service because of circular ref
    private final UserService userService;
    //    private final RoleService roleService;
    private final UserRestaurantRoleRepository roleRepository;
    private final AuthenticationService authenticationService;

    public CreateRestaurantGroupResponse createNewRestaurantGroup(CreateRestaurantGroupRequest request) {
        RestaurantGroup newRestaurantGroup =
                RestaurantGroup.builder()
                        .name(request.getGroupName())
                        .build();
        newRestaurantGroup = restaurantGroupRepository.save(newRestaurantGroup);

        Restaurant firstRestaurant =
                Restaurant.builder()
                        .name(request.getFirstRestaurant().getName())
                        .restaurantGroup(newRestaurantGroup)
                        .build();
        firstRestaurant = restaurantRepository.save(firstRestaurant);

        if (request.isNewAccount()) {
            AuthenticationResponse authenticationResponse =
                    authenticationService.generateTokenForNewUser(
                            UserRequest.builder()
                                    .email(request.getAdminUserEmail())
                                    .password(request.getAdminPassword())
                                    .build());
            User user = userService.findUserByEmail(authenticationResponse.getUserEmail());
            roleRepository.save(UserRestaurantRole.builder()
                    .user(user)
                    .restaurant(firstRestaurant)
                    .role(Role.ADMIN)
                    .build());
            return CreateRestaurantGroupResponse.builder()
                    .newRestaurantGroupId(newRestaurantGroup.getId())
                    .newRestaurantId(firstRestaurant.getId())
                    .auth(authenticationResponse)
                    .build();
        }
        User user = userService.findUserByEmail(request.getAdminUserEmail());
        AuthenticationResponse authenticationResponse =
                authenticationService.getToken(request.getAdminUserEmail(), request.getAdminPassword());
        roleRepository.save(UserRestaurantRole.builder()
                .user(user)
                .restaurant(firstRestaurant)
                .role(Role.ADMIN)
                .build());
        return CreateRestaurantGroupResponse.builder()
                .newRestaurantGroupId(newRestaurantGroup.getId())
                .newRestaurantId(firstRestaurant.getId())
                .auth(authenticationResponse)
                .build();
    }

    public RestaurantGroup findRestaurantGroupById(Long restaurantGroupId) {
        Optional<RestaurantGroup> groupOptional = restaurantGroupRepository.findById(restaurantGroupId);
        return NamiException.orElseThrow(groupOptional, ENTITY_NAME, restaurantGroupId.toString());
    }

    public void verifyRestaurantGroupExists(long restaurantGroupId) {
        if (!restaurantGroupRepository.existsById(restaurantGroupId)) {
            throw NamiException.notFound(ENTITY_NAME, restaurantGroupId);
        }
    }

    public List<RestaurantGroup> findAllRestaurantGroups() {
        return StreamUtil.transformIterableToList(restaurantGroupRepository.findAll());
    }

    public RestaurantGroup updateRestaurantGroup(RestaurantGroup restaurantGroup) {
        verifyRestaurantGroupExists(restaurantGroup.getId());
        return restaurantGroupRepository.save(restaurantGroup);
    }

    public void deleteRestaurantGroupAndCascade(Long restaurantGroupId) {
        RestaurantGroup restaurantGroup = findRestaurantGroupById(restaurantGroupId);
        restaurantGroupRepository.delete(restaurantGroup);
    }

}
