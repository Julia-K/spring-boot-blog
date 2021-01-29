package pl.kozlowska.blog.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.kozlowska.blog.dto.PostDto;
import pl.kozlowska.blog.models.Author;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.models.PostsAuthors;
import pl.kozlowska.blog.models.PostsAuthorsId;
import pl.kozlowska.blog.service.interfaces.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostsAuthorsService paService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthorService authorService;

    @PostMapping("/newPost")
    public String addNewPost(@RequestBody PostDto postDao) {
        if (!authorService.existsByUsername(postDao.getUsername())) {
            return "User does not exist!";
        }
        Author author = authorService.findByUsername(postDao.getUsername());

        Set<String> set = new HashSet<>();
        for (String t : postDao.getPostTags()) {
            if (t.contains(" ")) {
                Collections.addAll(set, t.split(" "));
            } else {
                set.add(t);
            }
        }

        String tags = set.stream().collect(
                Collectors.joining(" "));
        Post post = new Post();
        post.setTags(tags);
        post.setPostContent(postDao.getContent());
        postService.save(post);
        authorService.save(author);
        PostsAuthorsId postsAuthorsId = new PostsAuthorsId(post.getId(), author.getId());
        paService.save(new PostsAuthors(postsAuthorsId, post, author));
        postService.findById(post.getId()).get().setPostsAuthors(paService.findAllByPost(post));
        return "Post added successfully";
    }

    @PutMapping("/addTags")
    public String addTags(@RequestBody PostDto post) {
        if(!postService.existsById(post.getId())) {
            return "The post does not exist";
        }
        Post currPost = postService.findById(post.getId()).get();
        Set<String> set = new HashSet<>();
        for (String t : post.getPostTags()) {
            if(t.contains(" ")) {
                Collections.addAll(set, t.split(" "));
            } else {
                set.add(t);
            }
        }
        Collections.addAll(set, currPost.getTags().split(" "));
        String tags = set.stream().collect(Collectors.joining(" "));
        currPost.setTags(tags);
        postService.save(currPost);
        return "Tags added successfully";
    }

    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable int id) {
        attachmentService.deleteAllByPostId(id);
        commentService.deleteAllByPostId(id);
        paService.deleteAllByPostId(id);
        postService.deleteById(id);
        return "Post deleted successfully";
    }

    @PutMapping("/posts/{id}")
    public String editPost(@PathVariable int id, @RequestBody PostDto post) {
        if(!postService.existsById(id)) {
            return "Post does not exist!";
        }
        Post newPost = postService.findById(id).get();
        newPost.setPostContent(post.getContent());
        postService.save(newPost);
        return "The content of the post has been changed.";
    }

    @GetMapping("/posts")
    public List<Post> sort(@RequestParam(value="sort") String sort) {
        return postService.findAllBy(sort);
    }

    @GetMapping("/posts/authors/{id}")
    public List<Post> displayPostsByAuthorId(@PathVariable int id) {
        if(paService.findAllByAuthor(authorService.findById(id).get())==null) {
            return new ArrayList<>();
        }
        List<PostsAuthors> postsAuthors = paService.findAllByAuthor(authorService.findById(id).get());
        return postsAuthors.stream().map(PostsAuthors::getPost).collect(Collectors.toList());
    }

/*    @GetMapping("/api/post/{id}")
    public @ResponseBody Optional<Post> getPost(@PathVariable int id) {
       return postService.findById(id);
    }

    @GetMapping("/api/post/author/{id}")
    public List<Post> getPosts(@PathVariable int id) {
        return authorService.findById(id).get().getPostsAuthors()
                .stream()
                .map(PostsAuthors::getPost)
                .collect(Collectors.toList());
    }*/




}
