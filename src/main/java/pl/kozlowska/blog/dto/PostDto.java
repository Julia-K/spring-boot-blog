package pl.kozlowska.blog.dto;

import lombok.*;
import pl.kozlowska.blog.models.Attachment;
import pl.kozlowska.blog.models.Comment;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class PostDto {

    @Id
    @NonNull
    private int id;

    @NonNull
    @Size(min = 2, message = "Post content must be at least two characters long.")
    private String content;

    @NonNull
    private List<String> postTags;

    private String tags;

    private String username;

    private List<Integer> authorsId;

    private List<Comment> comments;

    private List<Attachment> attachments;

    public PostDto(String username, String content, List<String> postTags) {
        this.username = username;
        this.content = content;
        this.postTags = postTags;
    }
}
