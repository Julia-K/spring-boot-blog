package pl.kozlowska.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.kozlowska.blog.models.Attachment;
import pl.kozlowska.blog.models.Post;
import pl.kozlowska.blog.repository.AttachmentRepository;
import pl.kozlowska.blog.repository.PostRepository;
import pl.kozlowska.blog.service.interfaces.AttachmentService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepo;

    @Autowired
    List<Attachment> attachments;

    @Autowired
    PostRepository postRepository;

    @Override
    public Attachment findById(int id) {
        return attachmentRepo.findById(id);
    }

    @Override
    public List<Attachment> findAll() {
        return attachmentRepo.findAll();
    }

    @Override
    public List<Attachment> saveAll() {
        return attachmentRepo.saveAll(attachments);
    }

    @Override
    public Attachment save(MultipartFile file, int id) {
           return attachmentRepo.save(new Attachment(id, file.getName()));
    }

    @Override
    public void deleteAllByPostId(int id) {
        if(attachmentRepo.existsByPostId(id)) {
            attachmentRepo.deleteAllByPostId(id);
        }
    }

    @Override
    public Attachment save(Attachment attachment) {
        return attachmentRepo.save(attachment);
    }

    public void saveAllAttachmentsToPost() {
        for (Post p : postRepository.findAll()) {
            if(attachmentRepo.findAllByPostId(p.getId()).size()!=0) {
                Post post = p;
                post.setAttachments(attachmentRepo.findAllByPostId(p.getId()));
                postRepository.save(post);
                for (Attachment a : attachmentRepo.findAllByPostId(p.getId())) {
                    Attachment attachment = a;
                    attachment.setPost(postRepository.findById(a.getPostId()).get());
                    attachmentRepo.save(attachment);
                }
            }
        }
    }

    @Override
    public void deleteById(int id) {
        attachmentRepo.deleteById(id);
    }

    @PostConstruct
    public void init() {
        saveAll();
        saveAllAttachmentsToPost();
    }
}
