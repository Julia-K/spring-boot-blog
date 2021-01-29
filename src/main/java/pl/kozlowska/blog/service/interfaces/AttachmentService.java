package pl.kozlowska.blog.service.interfaces;

import org.springframework.web.multipart.MultipartFile;
import pl.kozlowska.blog.models.Attachment;

import java.util.List;

public interface AttachmentService {
    Attachment findById(int id);

    List<Attachment> findAll();

    List<Attachment> saveAll();

    Attachment save(MultipartFile file, int id);

    Attachment save(Attachment attachment);

    void deleteById(int id);

    void deleteAllByPostId(int id);
}
