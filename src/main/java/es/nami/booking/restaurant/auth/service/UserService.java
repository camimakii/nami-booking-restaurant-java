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

    public User registerOneUser(User user) {
        user.setId(null);
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return NamiException.orElseThrow(userOptional, ENTITY_NAME, email);
    }

}
