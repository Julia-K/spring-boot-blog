package pl.kozlowska.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.models.PostsAuthors;
import pl.kozlowska.blog.models.PostsAuthorsId;
import pl.kozlowska.blog.repository.AuthorRepository;
import pl.kozlowska.blog.repository.PostRepository;
import pl.kozlowska.blog.repository.PostsAuthorsRepository;
import pl.kozlowska.blog.service.interfaces.PostsAuthorsService;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Transactional
public class PostsAuthorsServiceImpl implements PostsAuthorsService {

    private AuthorRepository authorRepository;
    private PostRepository postRepository;
    private PostsAuthorsRepository postsAuthorsRepository;
    private List<PostsAuthorsId> postsAuthorsIds;

    @Autowired
    public PostsAuthorsServiceImpl(AuthorRepository authorRepository, PostRepository postRepository, PostsAuthorsRepository postsAuthorsRepository, List<PostsAuthorsId> postsAuthorsIds) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
        this.postsAuthorsRepository = postsAuthorsRepository;
        this.postsAuthorsIds = postsAuthorsIds;
    }

    @Override
    public void saveAll() {
        for (PostsAuthorsId p : postsAuthorsIds) {
            postsAuthorsRepository.saveAndFlush(new PostsAuthors(p, postRepository.findById(p.getPAIpostId()).get(),authorRepository.findById(p.getPAIauthorId()).get()));
        }
    }

    public void saveAllPostsToAuthors() {
        for (Author a : authorRepository.findAll()) {
            if(postsAuthorsRepository.findAllByAuthor(a).size()!=0) {
                a.setPostsAuthors(postsAuthorsRepository.findAllByAuthor(a));
            }
        }
    }

    public List<PostsAuthors> findAllByPost(Post post) {
        return postsAuthorsRepository.findAllByPost(post);
    }

    @Override
    public List<PostsAuthors> findAllByAuthor(Author author) {
        return postsAuthorsRepository.findAllByAuthor(author);
    }

    @Override
    public void deleteAllByPostId(int id) {
        postsAuthorsRepository.deleteAllByPostId(id);
    }

    public void saveAllAuthorsToPosts() {
        for (Post p : postRepository.findAll()) {
            if(postsAuthorsRepository.findAllByPost(p).size()!=0) {
                p.setPostsAuthors(postsAuthorsRepository.findAllByPost(p));
            }
        }
    }

    @Override
    public List<PostsAuthors> findAllByAuthorId(int id) {
       return postsAuthorsRepository.findAllByAuthorId(id);
    }

    @Override
    public PostsAuthors save(PostsAuthors postsAuthors) {
        return postsAuthorsRepository.save(postsAuthors);
    }

    @PostConstruct
    public void init() {
        saveAll();
        saveAllPostsToAuthors();
        saveAllAuthorsToPosts();
    }
}
