package pl.kozlowska.blog.service.interfaces;

import pl.kozlowska.blog.models.Comment;
import pl.kozlowska.blog.models.Post;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> saveAll();

    Comment save(Comment comment);

    Optional<Comment> findById(int id);

    List<Comment> findAll();

    void deleteById(int id);

    void deleteAllByPostId(int id);

    List<Comment> findAllByUsername(String username);

    boolean existsByUsername(String username);

    }
