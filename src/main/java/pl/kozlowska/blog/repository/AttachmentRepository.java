package pl.kozlowska.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kozlowska.blog.models.Attachment;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    Attachment findById(int id);
    List<Attachment> findAllByPostId(int id);
    void deleteAllByPostId(int id);
    boolean existsByPostId(int id);
}
