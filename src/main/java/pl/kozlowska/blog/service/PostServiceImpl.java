package pl.kozlowska.blog.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.repository.PostRepository;
import pl.kozlowska.blog.service.interfaces.PostService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Data
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private List<Post> posts;

    public PostServiceImpl(PostRepository postRepository, List<Post> posts) {
        this.postRepository = postRepository;
        this.posts = posts;
    }

    @Override
    public Optional<Post> findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAllBy(String sort) {
        switch (sort) {
            case "postsASC":
                return findAllByAscendingOrder();
            case "postsCommDESC":
                return findAllByDescendingCommentsNumber();
            case "postsCommASC":
                return findAllByAscendingCommentsNumber();
            case "postsAttDESC":
                return findAllByDescendingAttachmentsNumber();
            case "postsAttASC":
                return findAllByAscendingAttachmentsNumber();
            default:
                return findAllByDescendingOrder();
        }
    }

    @Override
    public List<Post> findAllByDescendingOrder() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public List<Post> findAllByAscendingOrder() {
        return postRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }


    @Override
    public List<Post> findAllByDescendingAttachmentsNumber() {
        return postRepository.findAllByDescendingAttachmentsNumber();
    }

    @Override
    public List<Post> findAllByAscendingAttachmentsNumber() {
        return postRepository.findAllByAscendingAttachmentsNumber();
    }

    @Override
    public List<Post> findAllByPostContentContainingOrderByIdDesc(String content) {
        return postRepository.findAllByPostContentContainingOrderByIdDesc(content);
    }

    @Override
    public List<Post> findAllByTagsIsContaining(String tag) {
        return postRepository.findByTagsContaining(tag);
    }

    @Override
    public Post save(Post post) {
        return postRepository.saveAndFlush(post);
    }

    @Override
    public void deleteById(int id) {
        postRepository.deleteById(id);
    }

    @Override
    public List<Post> saveAll() {
        return postRepository.saveAll(posts);
    }

    @Override
    public List<Post> findAllByDescendingCommentsNumber() {
        return postRepository.findAllByDescendingCommentsNumber();
    }

    @Override
    public List<Post> findAllByAscendingCommentsNumber() {
        return postRepository.findAllByAscendingCommentsNumber();
    }

    @PostConstruct
    public void init() {
        saveAll();
    }

    @Override
    public boolean existsById(int id) {
        return postRepository.existsById(id);
    }
}
