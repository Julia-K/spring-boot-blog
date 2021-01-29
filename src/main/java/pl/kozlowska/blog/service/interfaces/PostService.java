package pl.kozlowska.blog.service.interfaces;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    Optional<Post> findById(int id);

    List<Post> findAll();

    List<Post> findAllByAscendingOrder();

    List<Post> findAllByDescendingOrder();

    List<Post> findAllBy(String sort);

    boolean existsById(int id);

    Post save(Post post);

    void deleteById(int id);

    List<Post> saveAll();

    List<Post> findAllByAscendingCommentsNumber();

    List<Post> findAllByDescendingAttachmentsNumber();

    List<Post> findAllByAscendingAttachmentsNumber();

    List<Post> findAllByTagsIsContaining(String tag);

    List<Post> findAllByPostContentContainingOrderByIdDesc(String content);

    List<Post> findAllByDescendingCommentsNumber();
}
