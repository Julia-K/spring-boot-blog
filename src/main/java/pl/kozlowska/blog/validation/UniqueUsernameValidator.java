package pl.kozlowska.blog.validation;

import org.springframework.beans.factory.annotation.Autowired;
import pl.kozlowska.blog.service.interfaces.AuthorService;
import pl.kozlowska.blog.service.interfaces.CommentService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<ValidUniqueUsername, String> {

    @Autowired
    public AuthorService authorService;

    @Override
    public void initialize(ValidUniqueUsername arg) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !authorService.existsByUsername(s);
    }
}
