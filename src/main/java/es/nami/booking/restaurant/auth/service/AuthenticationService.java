package es.nami.booking.restaurant.auth.service;

import es.nami.booking.restaurant.auth.data.Token;
import es.nami.booking.restaurant.auth.data.TokenRepository;
import es.nami.booking.restaurant.auth.data.User;
import es.nami.booking.restaurant.auth.dto.AuthenticationResponse;
import es.nami.booking.restaurant.auth.dto.UserRequest;
import es.nami.booking.restaurant.error.ErrorCode;
import es.nami.booking.restaurant.error.NamiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${application.security.pepper}")
    private String pepper;

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse generateNewTokenForNewUser(UserRequest request) {

        // 1. Create new user in DB with encoded password
        User newUser = new User();
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encodeSeasonedPassword(request.getPassword()));
        newUser = userService.registerOneUser(newUser);

        // 2. Create token
        String token = jwtService.generateToken(newUser);
        String refreshToken = jwtService.generateRefreshToken(newUser);
        saveUserToken(newUser, token);

        // 3. Return the token
//        return token;
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();

    }

    public AuthenticationResponse getToken(String email, String password) {

        // 1. Verify credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        seasonPassword(password)
                )
        );

        // 2. Generate new tokens
        User user = userService.findUserByEmail(email);
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 3. Invalidate old valid tokens of the user
        revokeAllUserTokens(user);

        // 4. Save new generated valid token
        saveUserToken(user, token);

        // 5. Return tokens
        return AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

//    public boolean isTokenValid(String authHeader) {
//
//        // 1. Header not valid
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new NamiException(ErrorCode.NOT_AUTHORIZED);
//        }
//
//        String token = authHeader.substring(7);
//        String userEmail = jwtService.extractUsername(token);
//
//        return jwtService.isTokenValid(token, userService.findUserByEmail(userEmail));
//    }

    public AuthenticationResponse refreshToken(
            String authHeader
    ) {
        //final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 1. Header not valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new NamiException(ErrorCode.FORBIDDEN);
        }

        String refreshToken = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(refreshToken);

        // Token doesn't have a user linked
        if (userEmail == null) {
            throw new NamiException(ErrorCode.FORBIDDEN);
        }

        User user = userService.findUserByEmail(userEmail);

        if (jwtService.isTokenValid(refreshToken, user)) {
            String accessToken = jwtService.generateToken(user);

            revokeAllUserTokens(user);
            saveUserToken(user, accessToken);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new NamiException(ErrorCode.FORBIDDEN);
        }

    }

    private String seasonPassword(String password) {
        return password + pepper;
    }

    private String encodeSeasonedPassword(String password) {
        return passwordEncoder.encode(seasonPassword(password));
    }

}


