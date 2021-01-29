package pl.kozlowska.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kozlowska.blog.models.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Optional<Author> findById(int id);
    Author findByUsername(String username);
    Boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
