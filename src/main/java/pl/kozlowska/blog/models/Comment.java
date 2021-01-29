package pl.kozlowska.blog.models;

import lombok.*;
import pl.kozlowska.blog.validation.ValidUniqueUsername;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "comments")
public class Comment {

    @Id
    @NonNull
    @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, message = "Username must be at least 2 letters long.")
    @NonNull
    @Getter @Setter
    private String username;

    @NonNull
    @Getter @Setter
    private int idPost;

    @Size(min = 2, message = "Comment content must be longer.")
    @NonNull
    @Getter @Setter
    @Column(columnDefinition = "TEXT")
    private String commentContent;

    @Getter @Setter
    @ManyToOne
    private Post post;
}
