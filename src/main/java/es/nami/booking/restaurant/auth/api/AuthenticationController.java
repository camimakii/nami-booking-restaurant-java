package es.nami.booking.restaurant.auth.api;

import es.nami.booking.restaurant.auth.dto.AuthenticationResponse;
import es.nami.booking.restaurant.auth.dto.UserRequest;
import es.nami.booking.restaurant.auth.service.AuthenticationService;
import es.nami.booking.restaurant.util.Constants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_URL + "auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/token")
    public ResponseEntity<AuthenticationResponse> getToken(
            @RequestParam String email,
            @RequestParam String password
    ) {
        return ResponseEntity.ok(authenticationService.getToken(email, password));
    }

    @PostMapping("/user")
    public ResponseEntity<AuthenticationResponse> createNewUser(
            @RequestBody UserRequest userRequest
    ) {
        return ResponseEntity.ok(authenticationService.generateNewTokenForNewUser(userRequest));
    }

    @GetMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return ResponseEntity.ok(authenticationService.refreshToken(authHeader));
    }

}
