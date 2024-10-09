package uz.pdp.springsecurity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Dismissal;

import java.util.List;
import java.util.UUID;

public interface DismissalRepository extends JpaRepository<Dismissal, UUID> {
    List<Dismissal> findAllByBusinessIdOrderByCreatedAtDesc(UUID businessId);
}
