package pl.kozlowska.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kozlowska.blog.validation.ValidPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    @NotNull
    @NotEmpty(message = "Last name is required")
    @Size(min = 2, message = "First name must be at least 2 characters long.")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    @Size(min = 2, message = "First name must be at least 2 characters long.")
    private String lastName;

    @NotNull
    @Size(min = 2, message = "Username must be at least 2 characters long.")
    private String username;

    @Email(message = "Invalid email format")
    @NotNull
    private String email;

    @NotNull
    @ValidPassword
    private String password;
}
