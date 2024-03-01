package es.nami.booking.restaurant.auth.service;

import es.nami.booking.restaurant.auth.data.User;
import es.nami.booking.restaurant.auth.data.UserRepository;
import es.nami.booking.restaurant.error.NamiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String ENTITY_NAME = "User";

    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findById(email);
        return NamiException.orElseThrow(userOptional, ENTITY_NAME, email);
    }

    public Optional<User> findOptionalUserByEmail(String email) {
        return userRepository.findById(email);
    }

}
