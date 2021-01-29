package pl.kozlowska.blog.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kozlowska.blog.models.Attachment;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.service.interfaces.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@RestController()
@RequestMapping("/api")
public class AttachmentController {

    @Autowired
    AuthorService authorService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PostService postService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private CommentService commentService;

    @DeleteMapping("/attachments/{id}")
    public String deleteAttachment(@PathVariable int id) {
        attachmentService.deleteById(id);
        return "Attachment deleted successfully";
    }

    @PostMapping("/posts/{id}/attachments")
    public String addNewAttachment(@PathVariable int id, @RequestParam("files") MultipartFile[] files) {
        Post post = postService.findById(id).get();
        if (!Objects.equals(files[0].getOriginalFilename(), "")) {
            for (MultipartFile f : files) {
                Attachment attachment = new Attachment(post.getId(), f.getOriginalFilename());
                attachment.setPost(post);
                storageService.store(f);
                attachmentService.save(attachment);
                post.setAttachments(new ArrayList<>(Arrays.asList(attachment)));
            }
        }
        return "Attachments added successfully";
    }

    @GetMapping("/attachments/{id}")
    public Resource downloadAttachment(@PathVariable int id) {
        String filename = attachmentService.findById(id).getFilename();
        return storageService.loadAsResource(filename);
    }
}
