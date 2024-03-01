package es.nami.booking.restaurant.auth.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join User u\s
            on t.user.id = u.id\s
            where u.id = :userId and (t.expired = false or t.revoked = false)\s
            """)
    List<Token> findAllValidTokenByUser(Long userId);

    Optional<Token> findByToken(String token);

}
