package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Supplier;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier,UUID> {

    List<Supplier> findAllByBusinessId(UUID business_id);

    @Query(nativeQuery = true, value = "Select sum(debt) as totalOurMoney from supplier where debt < 0 and business_id = :businessId")
    Double allOurMoney(UUID businessId);

    @Query(nativeQuery = true, value = "Select sum(debt) as totalOurMoney from supplier where debt > 0 and business_id = :businessId")
    Double allYourMoney(UUID businessId);

    @Query("select count(s) from Supplier s where s.createdAt between :startDate and :endDate")
    long countTotalBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
