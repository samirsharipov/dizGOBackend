package uz.dizgo.erp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Dismissal;

import java.util.List;
import java.util.UUID;

public interface DismissalRepository extends JpaRepository<Dismissal, UUID> {
    List<Dismissal> findAllByBusinessIdOrderByCreatedAtDesc(UUID businessId);
}
