package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.FileData;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface FileDateRepository extends JpaRepository<FileData, UUID> {
    List<FileData> findAllByTask_Id(UUID task_id);

    List<FileData> findAllByUser_Id(UUID user_id);
}
