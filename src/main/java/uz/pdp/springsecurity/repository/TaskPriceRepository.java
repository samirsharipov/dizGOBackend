package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.TaskPrice;

import java.util.List;
import java.util.UUID;

public interface TaskPriceRepository extends JpaRepository<TaskPrice, UUID> {
}
