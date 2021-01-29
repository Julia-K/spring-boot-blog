package pl.kozlowska.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.kozlowska.blog.models.Post;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findById (int id);

    List<Post> findByTagsContaining(String tag);

    List<Post> findAllByPostContentContainingOrderByIdDesc(String content);

    @Query(value = "select p from Post p left outer join p.comments cm on cm.idPost=p.id\n" +
                    "group by p.id order by count(cm.id) desc")
    List<Post> findAllByDescendingCommentsNumber();

    @Query(value = "select p from Post p left outer join p.comments cm on cm.idPost=p.id\n" +
                    "group by p.id order by count(cm.id) asc")
    List<Post> findAllByAscendingCommentsNumber();

    @Query(value = "select p from Post p left outer join p.attachments att on att.postId=p.id\n" +
            "group by p.id order by count(att.id) desc")
    List<Post> findAllByDescendingAttachmentsNumber();

    @Query(value = "select p from Post p left outer join p.attachments att on att.postId=p.id\n" +
            "group by p.id order by count(att.id) asc")
    List<Post> findAllByAscendingAttachmentsNumber();
}
