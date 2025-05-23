package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.Reason;
import uz.dizgo.erp.payload.ReasonDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public interface ReasonRepository extends JpaRepository<Reason, UUID> {
    @Transactional
    @Modifying
    @Query("UPDATE Reason r SET r.active = false WHERE r.id = :reasonId")
    void softDeleteById(@Param("reasonId") UUID reasonId);

    @Query("SELECT new uz.dizgo.erp.payload.ReasonDto(r.id, r.name, r.businessId) " +
            "FROM Reason r WHERE r.businessId = :businessId ORDER BY r.name ASC")
    List<ReasonDto> findByBusinessIdOrderByNameAsc(UUID businessId);
}
