package pl.kozlowska.blog.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kozlowska.blog.dto.PostDto;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.PostsAuthors;
import pl.kozlowska.blog.models.PostsAuthorsId;
import pl.kozlowska.blog.service.interfaces.AuthorService;
import pl.kozlowska.blog.service.interfaces.PostService;
import pl.kozlowska.blog.service.interfaces.PostsAuthorsService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller("authorwebcontroller")
public class AuthorController {

    private PostService postService;
    private AuthorService authorService;
    private PostsAuthorsService paService;

    @Autowired
    public AuthorController(PostService postService, AuthorService authorService, PostsAuthorsService postsAuthorsService) {
        this.postService = postService;
        this.authorService = authorService;
        this.paService = postsAuthorsService;
    }

    @GetMapping("/post/{id}/author/add/")
    public String addAuthor(Model model, @PathVariable int id) {
        if (authorService.findById(id).isPresent()) {
            List<Author> toSelect = authorService.findAll();
            List<Integer> existingAuthors = postService.findById(id).get().getPostsAuthors().stream().map(e -> e.getAuthor().getId())
                    .collect(Collectors.toList());
            List<Author> test = postService.findById(id).get().getPostsAuthors().stream().map(e -> e.getAuthor())
                    .collect(Collectors.toList());
            toSelect.removeAll(test);
            model.addAttribute("authors", toSelect);
            model.addAttribute("postId", String.valueOf(id));
            model.addAttribute("postDao", new PostDto());
            model.addAttribute("existingAuthors", existingAuthors);
        }
        return "author-add";
    }

    @PostMapping("/post/{Pid}/author/addExisting")
    public String saveExistingAuthor(PostDto postDao, @PathVariable int Pid) {
        for (int authorId: postDao.getAuthorsId()) {
            PostsAuthorsId postsAuthorsId = new PostsAuthorsId(postService.findById(Pid).get().getId(), authorService.findById(authorId).get().getId());
            paService.save(new PostsAuthors(postsAuthorsId, postService.findById(Pid).get(), authorService.findById(authorId).get()));
            postService.findById(Pid).get().setPostsAuthors(paService.findAllByPost(postService.findById(Pid).get()));
            authorService.findById(authorService.findById(authorId).get().getId()).get().setPostsAuthors(paService.findAllByAuthor(authorService.findById(authorId).get()));
        }
        return "redirect:/post/"+Pid;
    }

    @PostMapping("/post/{id}/author/add/")
    public String saveNewAuthor(@Valid Author author, BindingResult br, @PathVariable int id) {
        if(br.hasErrors()) {
            return "author-add";
        }
        Author author1 = new Author();
        author1.setUsername(author.getUsername());
        author1.setFirstName(author.getFirstName());
        author1.setLastName(author.getLastName());
        authorService.save(author1);
        PostsAuthorsId postsAuthorsId = new PostsAuthorsId(postService.findById(id).get().getId(), author1.getId());
        paService.save(new PostsAuthors(postsAuthorsId, postService.findById(id).get(), author1));
        postService.findById(id).get().setPostsAuthors(paService.findAllByPost(postService.findById(id).get()));
        authorService.findById(author1.getId()).get().setPostsAuthors(paService.findAllByAuthor(author1));
        return "redirect:/post/"+id;
    }

    @GetMapping("/posts/authors/{authorId}")
    public String getAuthorPosts(Model model, @PathVariable int authorId) {
        List<PostsAuthors> postsAuthors = paService.findAllByAuthor(authorService.findById(authorId).get());
        model.addAttribute("author",authorService.findById(authorId).get());
        model.addAttribute("posts", postsAuthors.stream().map(PostsAuthors::getPost).collect(Collectors.toList()));
        return "author";
    }

    @ModelAttribute("authUser")
    public Author authentication(Authentication authentication) {
        if (authentication != null)
            return authorService.findByUsername(authentication.getName());
        else
            return null;
    }
}
