package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.FileData;

import java.util.List;
import java.util.UUID;

public interface FileDateRepository extends JpaRepository<FileData, UUID> {
    List<FileData> findAllByTask_Id(UUID task_id);

    Page<FileData> findAllByUser_Id(UUID user_id, Pageable pageable);

    Page<FileData> findAllByUser_IdAndFileNameContainingIgnoreCase(UUID userId, String fileName, Pageable pageable);

}
