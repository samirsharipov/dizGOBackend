package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.PocketMoneyTransaction;

import java.util.List;
import java.util.UUID;

public interface PocketMoneyTransactionRepository extends JpaRepository<PocketMoneyTransaction, UUID> {
    List<PocketMoneyTransaction> findAllByCashierUserId(UUID cashierUserId);
}
