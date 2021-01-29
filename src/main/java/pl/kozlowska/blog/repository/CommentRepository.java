package pl.kozlowska.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kozlowska.blog.models.Comment;
import pl.kozlowska.blog.models.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByIdPost(int id);

    Optional<Comment> findById(int id);

    void deleteAllByPostId(int id);

    List<Comment> findAllByUsername(String username);

    boolean existsByUsername(String username);

}
