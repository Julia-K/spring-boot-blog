package pl.kozlowska.blog.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PostsAuthorsId implements Serializable {

    @NonNull
    @Column(name = "id_post")
    @Getter @Setter
    private int PAIpostId;

    @NonNull
    @Column(name = "id_author")
    @Getter @Setter
    private int PAIauthorId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostsAuthorsId that = (PostsAuthorsId) o;
        return PAIpostId == that.PAIpostId &&
                PAIauthorId == that.PAIauthorId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(PAIpostId, PAIauthorId);
    }
}
