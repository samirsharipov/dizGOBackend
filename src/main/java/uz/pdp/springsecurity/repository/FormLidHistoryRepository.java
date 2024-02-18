package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.FormLidHistory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormLidHistoryRepository extends JpaRepository<FormLidHistory, UUID> {
    Optional<FormLidHistory> findByActiveIsTrue();

    List<FormLidHistory> findAllByBusinessIdOrderByCreatedAtAsc(UUID business_id);

    @Query(value = "SELECT SUM(total_summa) FROM form_lid_history WHERE created_at BETWEEN ?1 AND ?2 AND business_id = ?3", nativeQuery = true)
    Double lidPriceByCreatedAtBetweenAndBusinessId(Timestamp from, Timestamp to, UUID businessId);
}
