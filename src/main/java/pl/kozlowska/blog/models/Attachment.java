package pl.kozlowska.blog.models;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    private int postId;

    @NonNull
    private String filename;

    @ManyToOne
    private Post post;
}
