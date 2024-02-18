package uz.pdp.springsecurity.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.BalanceHistory;

import java.sql.Timestamp;
import java.util.UUID;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, UUID> {

    Page<BalanceHistory> findAllByBalance_Id(UUID balance_Id, Pageable pageable);
    Page<BalanceHistory> findAllByBalance_IdAndCreatedAtBetween(UUID balance_Id, Timestamp startDate, Timestamp endDate, Pageable pageable);
    Page<BalanceHistory> findAllByBalance_Branch_Id(UUID balance_branch_id, Pageable pageable);
    Page<BalanceHistory> findAllByBalance_Branch_IdAndCreatedAtBetween(UUID balance_branch_id, Timestamp startDate, Timestamp endDate, Pageable pageable);

}
