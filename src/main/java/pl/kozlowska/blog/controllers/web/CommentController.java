package pl.kozlowska.blog.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.Comment;
import pl.kozlowska.blog.service.interfaces.AuthorService;
import pl.kozlowska.blog.service.interfaces.CommentService;

import javax.validation.Valid;

@Controller("commentwebcontroller")
public class CommentController {

    private CommentService commentService;

    private AuthorService authorService;

    @Autowired
    public CommentController(CommentService commentService, AuthorService authorService) {
        this.commentService = commentService;
        this.authorService = authorService;
    }

    @GetMapping("/comments/{user}")
    public String getUserComments(Model model, @PathVariable String user) {
        model.addAttribute("comments", commentService.findAllByUsername(user));
        return "comments";
    }

    @GetMapping("/post/{id}/comment/delete/{commId}")
    public String deleteComment(@PathVariable int id, @PathVariable int commId) {
        commentService.deleteById(commId);
        return "redirect:/post/"+id;
    }

    @GetMapping("/post/{id}/comment/edit/{commId}")
    public String editComment(Model model, @PathVariable int commId) {
        if (commentService.findById(commId).isPresent()) {
            model.addAttribute("comment", commentService.findById(commId));
        }
        return "comment-edit";
    }

    @PostMapping("/post/{id}/comment/edit/{commId}")
    public String updatePost(@PathVariable int id, @PathVariable int commId, @Valid Comment comment, BindingResult br) {
        if(br.hasErrors()) {
            return "comment-edit";
        }
        if (commentService.findById(commId).isPresent()) {
            Comment currComment = commentService.findById(commId).get();
            currComment.setCommentContent(comment.getCommentContent());
            currComment.setUsername(comment.getUsername());
            commentService.save(currComment);
        }
        return "redirect:/post/"+id;
    }

    @ModelAttribute("authUser")
    public Author authentication(Authentication authentication) {
        if (authentication != null)
            return authorService.findByUsername(authentication.getName());
        else
            return null;
    }
}
