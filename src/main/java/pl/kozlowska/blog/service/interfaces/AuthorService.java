package pl.kozlowska.blog.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.dto.UserRegistrationDto;

import java.util.List;
import java.util.Optional;

public interface AuthorService extends UserDetailsService {
    Optional<Author> findById(int id);

    Author findByUsername(String username);

    List<Author> findAll();

    Author save(Author author);

    Author save(UserRegistrationDto userRegistrationDto);

    Author save(UserRegistrationDto userRegistrationDto, String role);

    List<Author> saveAll();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
