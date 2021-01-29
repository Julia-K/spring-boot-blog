package pl.kozlowska.blog.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import pl.kozlowska.blog.dto.UserRegistrationDto;
import pl.kozlowska.blog.service.interfaces.AuthorService;
import pl.kozlowska.blog.service.interfaces.PostService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("/api/auth")
public class AuthorController {

    private AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }


}
