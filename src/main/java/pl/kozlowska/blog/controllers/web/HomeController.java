package pl.kozlowska.blog.controllers.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.kozlowska.blog.service.interfaces.*;

@Slf4j
@Controller
public class HomeController {
    @Autowired
    AuthorService authorService;
    @Autowired
    PostService postService;
    @Autowired
    CommentService commentService;
    @Autowired
    AttachmentService attachmentService;

    @GetMapping("/")
    public String home() {
        return "redirect:/posts";
    }
}