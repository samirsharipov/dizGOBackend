package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Attachment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository extends JpaRepository<Attachment, UUID> {

    List<Attachment> findAllByName(String name);
    Optional<Attachment> findByName(String name);
    Optional<Attachment> findAllById(UUID id);
    void deleteByName(String name);
}
