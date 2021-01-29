package pl.kozlowska.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.models.PostsAuthors;
import pl.kozlowska.blog.models.PostsAuthorsId;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsAuthorsRepository extends JpaRepository<PostsAuthors, PostsAuthorsId> {

    List<PostsAuthors> findAllByAuthor(Author author);

    List<PostsAuthors> findAllByPost(Post post);

    List<PostsAuthors> findAllByAuthorId(int id);

    void deleteAllByPostId(int id);
}
