package pl.kozlowska.blog.models;

import lombok.*;

import javax.persistence.*;

@Table(name = "posts_authors")
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class PostsAuthors {

    @EmbeddedId
    @Getter @Setter
    private PostsAuthorsId id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("PApostId")
    @JoinColumn(name = "id_post")
    @Getter @Setter
    @NonNull
    Post post;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @MapsId("PAauthorId")
    @JoinColumn(name = "id_author")
    @Getter @Setter
    @NonNull
    Author author;
}
