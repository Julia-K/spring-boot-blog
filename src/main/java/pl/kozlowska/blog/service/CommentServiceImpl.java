package pl.kozlowska.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kozlowska.blog.models.Comment;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.repository.CommentRepository;
import pl.kozlowska.blog.repository.PostRepository;
import pl.kozlowska.blog.service.interfaces.CommentService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private List<Comment> comments;
    private PostRepository postRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, List<Comment> comments, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.comments = comments;
        this.postRepository = postRepository;
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }

    public void deleteAllByPostId(int id) {
        commentRepository.deleteAllByPostId(id);
    }

    @Override
    public List<Comment> findAllByUsername(String username) {
        return commentRepository.findAllByUsername(username);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

    @Override
    public List<Comment> saveAll() {
        return commentRepository.saveAll(comments);
    }

    public void saveAllCommentsToPost() {
        for (Post p : postRepository.findAll()) {
            if(commentRepository.findAllByIdPost(p.getId()).size()!=0) {
                Post post = p;
                post.setComments(commentRepository.findAllByIdPost(p.getId()));
                postRepository.save(post);
                for (Comment c : commentRepository.findAllByIdPost(p.getId())) {
                    Comment comment = c;
                    comment.setPost(postRepository.findById(c.getIdPost()).get());
                    commentRepository.save(comment);
                }
            }
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        return commentRepository.existsByUsername(username);
    }

    @PostConstruct
    public void init() {
        saveAll();
        saveAllCommentsToPost();
    }
}
