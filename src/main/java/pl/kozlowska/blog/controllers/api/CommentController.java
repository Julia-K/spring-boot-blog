package pl.kozlowska.blog.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.kozlowska.blog.dto.CommentDto;
import pl.kozlowska.blog.models.Comment;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.service.interfaces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    AuthorService authorService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/posts/{id}/comment")
    public String addComment(@PathVariable int id, @RequestBody CommentDto comment) {
        Comment newComment = new Comment();
        newComment.setCommentContent(comment.getCommentContent());
        newComment.setPost(postService.findById(id).get());
        newComment.setIdPost(id);
        newComment.setUsername(comment.getUsername());
        commentService.save(newComment);
        postService.findById(id).get().getComments().add(newComment);
        Post post = postService.findById(id).get();
        return "Comment added successfully";
    }

    @PutMapping("/comments/{id}")
    public String updateComment(@PathVariable int id, @RequestBody CommentDto comment) {
        Comment editedComment = commentService.findById(id).get();
        editedComment.setCommentContent(comment.getCommentContent());
        commentService.save(editedComment);
        return "Comment updated successfully";
    }

    @DeleteMapping("/comments/{id}")
    public String deleteComment(@PathVariable int id) {
        commentService.deleteById(id);
        return "Comment deleted successfully";
    }

    @GetMapping("/comments/authors/{username}")
    public List<Comment> displayCommentsByAuthorUsername(@PathVariable String username) {
        if(commentService.findAllByUsername(username)==null) {
            return new ArrayList<>();
        }
        return commentService.findAllByUsername(username);
    }

    @GetMapping("/comments/authors")
    public List<Comment> displayCommentsByAuthorUsernameAndLength(@RequestParam(value = "username") String username, @RequestParam(value = "length") int length) {
        if (commentService.findAllByUsername(username) == null) {
            return new ArrayList<>();
        }
        return commentService.findAllByUsername(username).stream().filter(e -> e.getCommentContent().length() >= length).collect(Collectors.toList());
    }
}
