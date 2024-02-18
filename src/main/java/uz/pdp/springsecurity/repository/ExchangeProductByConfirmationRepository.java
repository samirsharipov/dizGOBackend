package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ExchangeProductByConfirmation;

import java.util.List;
import java.util.UUID;

public interface ExchangeProductByConfirmationRepository extends JpaRepository<ExchangeProductByConfirmation, UUID> {
    List<ExchangeProductByConfirmation> findAllByExchangeProductBranch_BusinessId(UUID exchangeProductBranch_business_id);
}
