package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ExchangeStatus;

import java.util.UUID;

public interface ExchangeStatusRepository extends JpaRepository<ExchangeStatus, UUID> {
}
