package pl.kozlowska.blog.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.SpringTemplateEngine;
import pl.kozlowska.blog.service.interfaces.AuthorService;
import pl.kozlowska.blog.dto.UserRegistrationDto;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller("registrationcontroller")
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    @PostMapping
    public String registrationAuthor(@Valid @ModelAttribute("user") UserRegistrationDto registrationDto, BindingResult bindingResult) throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        authorService.save(registrationDto);
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();
        Map<String, Object> doubleBraceMap  = new HashMap<String, Object>() {{
            put("user", registrationDto);
        }};
        context.setVariables(doubleBraceMap);
        helper.setTo(registrationDto.getEmail());
        helper.setSubject("Successful registration\n");
        String html = templateEngine.process("email", context);
        helper.setText(html, true);
        emailSender.send(message);
        return "redirect:/registration?success";
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
}
