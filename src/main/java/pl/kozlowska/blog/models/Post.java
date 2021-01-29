package pl.kozlowska.blog.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@JsonPropertyOrder({"id","postContent","tags"})
public class Post {

    @Id
    @NonNull
    @Column(name = "post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Size(min = 2, message = "Post content must be at least 2 characters long.")
    @Column(columnDefinition = "TEXT", name = "postContent")
    private String postContent;

    @NonNull
    @Column(name = "tags")
    private String tags;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.MERGE})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<PostsAuthors> postsAuthors;

    @OneToMany(mappedBy = "post",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Attachment> attachments;
}
