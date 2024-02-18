package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ExchangeProductBranch;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface ExchangeProductBranchRepository extends JpaRepository<ExchangeProductBranch, UUID> {
    List<ExchangeProductBranch> findAllByExchangeDateAndBusiness_IdAndDeleteIsFalse(Date exchangeDate, UUID business_id);

    List<ExchangeProductBranch> findAllByBusiness_IdAndDeleteIsFalse(UUID business_id);
    List<ExchangeProductBranch> findAllByBusiness_IdAndDeleteIsNull(UUID business_id);

    List<ExchangeProductBranch> findAllByShippedBranch_IdAndDeleteIsFalse(UUID shippedBranch_id);
    List<ExchangeProductBranch> findAllByReceivedBranch_IdAndDeleteIsFalse(UUID receivedBranch_id);

    List<ExchangeProductBranch> findAllByExchangeStatus_IdAndBusiness_IdAndDeleteIsFalse(UUID exchangeStatusId, UUID business_id);
}
