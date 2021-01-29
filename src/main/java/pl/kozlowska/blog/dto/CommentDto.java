package pl.kozlowska.blog.dto;

import lombok.*;
import pl.kozlowska.blog.validation.ValidUniqueUsername;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {

    @Size(min = 2, message = "Username must be at least 2 letters long.")
    @NonNull
    @ValidUniqueUsername
    private String username;

    @Size(min = 2, message = "Comment content must be longer.")
    @NonNull
    private String commentContent;

    private int postId;
}
