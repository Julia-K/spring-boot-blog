package pl.kozlowska.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kozlowska.blog.models.Post;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;

    private List<Post> posts;
}
