package es.nami.booking.restaurant.client.api;

import es.nami.booking.restaurant.client.data.Role;
import es.nami.booking.restaurant.client.service.RoleService;
import es.nami.booking.restaurant.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_URL + "role")
@RequiredArgsConstructor
@Slf4j
public class RoleRestController {

    private final RoleService roleService;

    @PostMapping
    @Operation(summary = "POST Assign role to user in a restaurant", description = "Create or update a mapping User - Restaurant - Role")
    @ApiResponse(responseCode = "200", description = "Successfully created or updated")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity assignRole(
            @RequestParam String userEmail,
            @RequestParam long restaurantId,
            @RequestParam Role role,
            HttpServletRequest request
    ) {
        log.debug("[REQUEST] {} {} email: {} restaurant: {} role: {}", request.getMethod(), request.getRequestURI(), userEmail, restaurantId, role.name());
        return ResponseEntity.status(HttpStatus.OK).body(roleService.assignRole(userEmail, restaurantId, role));
    }

}
