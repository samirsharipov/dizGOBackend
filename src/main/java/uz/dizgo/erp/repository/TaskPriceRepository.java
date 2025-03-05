package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.TaskPrice;

import java.util.UUID;

public interface TaskPriceRepository extends JpaRepository<TaskPrice, UUID> {
}
