package pl.kozlowska.blog.service.interfaces;

import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.models.PostsAuthors;

import java.util.List;
import java.util.Optional;

public interface PostsAuthorsService {

    void saveAll();

    PostsAuthors save(PostsAuthors postsAuthors);

    List<PostsAuthors> findAllByPost(Post post);

    List<PostsAuthors> findAllByAuthor(Author author);

    List<PostsAuthors> findAllByAuthorId(int id);

    void deleteAllByPostId(int id);
}
