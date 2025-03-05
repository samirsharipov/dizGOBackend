package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.ExchangeStatus;

import java.util.UUID;

public interface ExchangeStatusRepository extends JpaRepository<ExchangeStatus, UUID> {
}
