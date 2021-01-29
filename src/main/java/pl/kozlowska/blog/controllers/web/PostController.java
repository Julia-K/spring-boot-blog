package pl.kozlowska.blog.controllers.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import pl.kozlowska.blog.models.*;
import pl.kozlowska.blog.dto.CommentDto;
import pl.kozlowska.blog.dto.PostDto;
import pl.kozlowska.blog.service.interfaces.*;
import pl.kozlowska.blog.storage.StorageFileNotFoundException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Controller("postwebcontroller")
public class PostController {
    private PostService postService;

    private AuthorService authorService;

    private PostsAuthorsService paService;

    private CommentService commentService;

    private StorageService storageService;

    private AttachmentService attachmentService;

    @Autowired
    public PostController(PostService postService,
                          AuthorService authorService,
                          PostsAuthorsService paService,
                          CommentService commentService,
                          StorageService storageService,
                          AttachmentService attachmentService) {
        this.postService = postService;
        this.authorService = authorService;
        this.paService = paService;
        this.commentService = commentService;
        this.storageService = storageService;
        this.attachmentService = attachmentService;
    }

    @GetMapping(value = "/posts/csv", produces="application/zip")
    @ResponseStatus(value = HttpStatus.OK)
    public void exportCSV(HttpServletResponse response) throws IOException {
        String[] files = {"posts.csv", "authors.csv", "attachments.csv", "comments.csv"};
        //response.setContentType("multipart/x-mixed-replace;boundary=END");
        writeToCsv();
        String zipSavedAt = "src\\main\\resources\\downloaded\\" + "files.zip"; // File saved location
        byte[] buffer = new byte[1024];
        FileOutputStream fos = new FileOutputStream(zipSavedAt);
        ZipOutputStream zos = new ZipOutputStream(fos);
        for (String fileName : files) {
            FileSystemResource resource = new FileSystemResource("src\\main\\resources\\downloaded\\" + fileName);
            Path path = storageService.loadCSV(fileName);   // Get the file from server from given path
            File file = path.toFile();
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zos.putNextEntry(zipEntry);
            StreamUtils.copy(fis, zos);
            zos.closeEntry();

        }
        zos.finish();
        zos.close();

            response.setContentType("application/octet-stream");
            try {
                String zipPath = zipSavedAt;
                File file1 = new File(zipPath);
                response.setHeader("Content-Length", String.valueOf(file1.length()));
                response.setHeader("Content-Disposition", "attachment; filename=\"" + "files.zip" + "\"");
                InputStream is = new FileInputStream(file1);
                FileCopyUtils.copy(IOUtils.toByteArray(is), response.getOutputStream());
                response.flushBuffer();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }



    @PostMapping("/posts")
    public String getSorting(@RequestParam String sort) {
        return "redirect:/posts/" + sort;
    }

    @ModelAttribute("authUser")
    public Author authentication(Authentication authentication) {
        if (authentication != null)
            return authorService.findByUsername(authentication.getName());
        else
            return null;
    }


    @GetMapping("/posts")
    public String getPosts(Model model, Authentication authentication) {
        model.addAttribute("posts", postService.findAllByDescendingOrder());
        model.addAttribute("files", storageService.loadAll()
                .filter(c -> c.getFileName().toString().matches("(.*[^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)"))
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList()));
        model.addAttribute("search","");
        return "home";
    }

    @GetMapping("/posts/search")
    public String getSearching(Model model, @RequestParam(value = "search", required = false) String q) {
        model.addAttribute("posts", postService.findAllByPostContentContainingOrderByIdDesc(q));
        return "home";
    }

    @GetMapping("/posts/{sort}")
    public String sortAsc(Model model, @PathVariable("sort") String sort) {
        model.addAttribute("posts", postService.findAllBy(sort));
        model.addAttribute("files", storageService.loadAll()
                .filter(c -> c.getFileName().toString().matches("(.*[^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)"))
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList()));
        return "home";
    }

    @GetMapping("/post/new")
    public String post(Model model) {
        List<String> tagsDuplication = new ArrayList<>();
        postService.findAll().forEach(t -> tagsDuplication.addAll(Arrays.asList(t.getTags().split(" "))));
        List<String> tags = new ArrayList<>(new HashSet<>(tagsDuplication));
        Collections.sort(tags);
        model.addAttribute("postDao", new PostDto());
        model.addAttribute("tags", tags);
        model.addAttribute("attachments", new ArrayList<Attachment>());
        return "post";
    }

    @PostMapping("/post/new")
    public String newPost(@Valid PostDto postDao, BindingResult br, @CurrentSecurityContext(expression="authentication.name")
            String username, BindingResult br2, @RequestParam("files") MultipartFile[] files) {
        if(br.hasErrors() || br2.hasErrors()) {
            return "post";
        }
        Author author = authorService.findByUsername(username);
        Set<String> set = new HashSet<>();
        for (String t : postDao.getPostTags()) {
            if(t.contains(" ")) {
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
        if (!Objects.equals(files[0].getOriginalFilename(), "")) {
            for (MultipartFile f : files) {
                Attachment attachment = new Attachment(post.getId(), f.getOriginalFilename());
                attachment.setPost(post);
                storageService.store(f);
                attachmentService.save(attachment);
                post.setAttachments(new ArrayList<>(Arrays.asList(attachment)));
            }
        }
        return "redirect:/posts";
    }
    @GetMapping(value = {"/files/{filename:.+}","/posts/files/{filename:.+}","/post/files/{filename:.+}"})
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    @GetMapping("/post/edit/{id}")
    public String editPost(Model model, @PathVariable int id) {
        Optional<Post> currentPost = postService.findById(id);
        if (currentPost.isPresent()) {
            List<String> tagsDuplication = new ArrayList<>();
            postService.findAll().forEach(t -> tagsDuplication.addAll(Arrays.asList(t.getTags().split(" "))));
            List<String> tags = new ArrayList<>(new HashSet<>(tagsDuplication));
            Collections.sort(tags);
            Post post = currentPost.get();
            PostDto postDao = new PostDto();
            postDao.setContent(post.getPostContent());
            postDao.setPostTags(Arrays.asList(post.getTags().split(" ")));
            model.addAttribute("post", postDao);
            model.addAttribute("tags", tags);
        }
        return "post-edit";
    }

    @PostMapping("/post/edit/{id}")
    public String updatePost(@PathVariable int id, @Valid @ModelAttribute PostDto post, BindingResult br) {
        if(br.hasErrors()) {
            return "post-edit";
        }
        Set<String> set = new HashSet<>();
        for (String t : post.getPostTags()) {
            if(t.contains(" ")) {
                Collections.addAll(set, t.split(" "));
            } else {
                set.add(t);
            }
        }
        Collections.addAll(set, post.getTags().split(" "));

        String tags = set.stream().collect(
                Collectors.joining(" "));

        Post currPost = postService.findById(id).get();
        currPost.setTags(tags);
        currPost.setPostContent(post.getContent());
        postService.save(currPost);
        return "redirect:/post/{id}";
    }

    @GetMapping("/post/delete/{id}")
    public String deletePostConfirmation(@PathVariable int id) {
        attachmentService.deleteAllByPostId(id);
        commentService.deleteAllByPostId(id);
        paService.deleteAllByPostId(id);
        postService.deleteById(id);
        return "redirect:/posts/";
    }

    @GetMapping("/post/{id}")
    public String getPost(Model model, @PathVariable int id) {
        Optional<Post> postOptional = postService.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            List<String> authors = post.getPostsAuthors().stream().map(e -> e.getAuthor().getUsername())
                    .collect(Collectors.toList());
            model.addAttribute("authors", authors);
            model.addAttribute("post", post);
            model.addAttribute("tags", Arrays.asList(post.getTags().split(" ")));
            model.addAttribute("comment", new CommentDto());
            model.addAttribute("files", storageService.loadAll()
                    .filter(c -> c.getFileName().toString().matches("(.*[^\\s]+(\\.(?i)(jpg|png|gif|bmp|jpeg))$)"))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList()));
        }
        return "post-display";
    }

    @PostMapping("/post/{id}")
    public String addComment(Model model, @PathVariable int id, @Valid @ModelAttribute("comment") CommentDto comment, BindingResult br) {
        if(br.hasErrors()) {
            model.addAttribute("post", postService.findById(id).get());
            return "post-display";
        }
        if (postService.findById(id).isPresent()) {
            Comment comment1 = new Comment();
            comment1.setCommentContent(comment.getCommentContent());
            comment1.setPost(postService.findById(id).get());
            comment1.setIdPost(id);
            commentService.save(comment1);
            postService.findById(id).get().getComments().add(comment1);
            if (getCurrentAuth().getPrincipal().equals("anonymousUser")) {
                comment1.setUsername(comment.getUsername());
            } else {
                String username = ((UserDetails)getCurrentAuth().getPrincipal()).getUsername();
                comment1.setUsername(username);
            }
            model.addAttribute("post", postService.findById(id).get());
            model.addAttribute("tags", Arrays.asList(postService.findById(id).get().getTags().split(" ")));
        }
        return "redirect:/post/{id}";
    }

    @GetMapping("/posts/tags/{tag}")
    public String displayWithTag(Model model, @PathVariable String tag) {
        model.addAttribute("posts", postService.findAllByTagsIsContaining(tag));
        model.addAttribute("oneTag", tag);
        return "tags";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    public void writeToCsv() throws IOException {
        String[] csvHeader = {"id", "postContent", "tags"};
        String[] nameMapping = {"id", "postContent", "tags"};
        String[] headerA = {"id","firstName","lastName","username"};
        String[] mappingA = {"id","firstName","lastName","username"};
        String[] headerAtt = {"id","postId","filename"};
        String[] mappingAtt = {"id","postId","filename"};
        String[] headerC = {"id","username","idPost","commentContent"};
        String[] mappingC = {"id","username","idPost","commentContent"};
        ICsvBeanWriter csvWriter = new CsvBeanWriter(new FileWriter("src\\main\\resources\\downloaded\\posts.csv"), CsvPreference.STANDARD_PREFERENCE);
        csvWriter.writeHeader(csvHeader);
        for (Post p : postService.findAll()) {
            csvWriter.write(p, nameMapping);
        }

        csvWriter.close();
        ICsvBeanWriter csvWriter2 = new CsvBeanWriter(new FileWriter("src\\main\\resources\\downloaded\\authors.csv"), CsvPreference.STANDARD_PREFERENCE);
        csvWriter2.writeHeader(headerA);
        for (Author a : authorService.findAll()) {
            csvWriter2.write(a, mappingA);
        }
        csvWriter2.close();

        ICsvBeanWriter csvWriter3 = new CsvBeanWriter(new FileWriter("src\\main\\resources\\downloaded\\comments.csv"), CsvPreference.STANDARD_PREFERENCE);
        csvWriter3.writeHeader(headerC);
        for (Comment a : commentService.findAll()) {
            csvWriter3.write(a, mappingC);
        }
        csvWriter3.close();

        ICsvBeanWriter csvWriter4 = new CsvBeanWriter(new FileWriter("src\\main\\resources\\downloaded\\attachments.csv"), CsvPreference.STANDARD_PREFERENCE);
        csvWriter4.writeHeader(headerAtt);
        for (Attachment a : attachmentService.findAll()) {
            csvWriter4.write(a, mappingAtt);
        }
        csvWriter4.close();
    }


    public Authentication getCurrentAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
