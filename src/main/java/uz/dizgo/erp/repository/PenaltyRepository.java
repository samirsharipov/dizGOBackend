package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Penalty;

import java.util.UUID;

public interface PenaltyRepository extends JpaRepository<Penalty, UUID> {
}