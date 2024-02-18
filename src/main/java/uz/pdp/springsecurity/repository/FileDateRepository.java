package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.FileData;

import java.util.List;
import java.util.UUID;

public interface FileDateRepository extends JpaRepository<FileData, UUID> {
    List<FileData> findByProjectId(UUID projectId);
    List<FileData> findAllByTask_Id(UUID task_id);
}
