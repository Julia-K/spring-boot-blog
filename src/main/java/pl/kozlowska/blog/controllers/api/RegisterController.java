package pl.kozlowska.blog.controllers.api;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import pl.kozlowska.blog.dto.CommentDto;
import pl.kozlowska.blog.dto.MessageResponse;
import pl.kozlowska.blog.dto.PostDto;
import pl.kozlowska.blog.dto.UserRegistrationDto;
import pl.kozlowska.blog.models.*;
import pl.kozlowska.blog.service.interfaces.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
public class RegisterController {
    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    AuthorService authorService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PostsAuthorsService paService;

    @PostMapping("/signup")
    public String registrationAuthor(@Valid @RequestBody UserRegistrationDto registrationDto) throws MessagingException {
        if (authorService.existsByUsername(registrationDto.getUsername())) {
            return "Error: Username is already taken!";
        }

        if (authorService.existsByEmail(registrationDto.getUsername())) {
            return "Error: Emails is already taken!";
        }

        Author user = new Author(registrationDto.getFirstName(),
                registrationDto.getLastName(), registrationDto.getUsername(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                Arrays.asList(new Role("ROLE_USER")));

        authorService.save(user);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        Map<String, Object> doubleBraceMap = new HashMap<String, Object>() {{
            put("user", registrationDto);
        }};
        context.setVariables(doubleBraceMap);
        helper.setTo(registrationDto.getEmail());
        helper.setSubject("Successful registration\n");
        String html = templateEngine.process("email", context);
        helper.setText(html, true);
        emailSender.send(message);
        return "User registered successfully!";
    }

    @GetMapping("/posts/authors")
    public List<Post> displayPostsByAuthorIdAndTag(@RequestParam(value = "username") String username,
                                                   @RequestParam(value = "tag") String tag) {
        if (paService.findAllByAuthor(authorService.findByUsername(username)) == null) {
            return new ArrayList<>();
        }

        List<PostsAuthors> postsAuthors = paService.findAllByAuthor(authorService.findByUsername(username));
        return postsAuthors.stream().map(PostsAuthors::getPost).filter(e -> e.getTags().contains(tag)).collect(Collectors.toList());
    }
}
