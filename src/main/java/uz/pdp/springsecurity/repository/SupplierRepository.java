package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Supplier;

import java.util.List;
import java.util.UUID;

public interface SupplierRepository extends JpaRepository<Supplier,UUID> {

//    @Query(value = "select * from supplier s where s.business_id = ?",nativeQuery = true)
//    List<Supplier> findAllByBusinessId(UUID businessId);

    List<Supplier> findAllByBusinessId(UUID business_id);
    @Query(nativeQuery = true, value = "Select sum(debt) as totalOurMoney from supplier where debt < 0 and business_id = :businessId")
    Double allOurMoney(UUID businessId);
    @Query(nativeQuery = true, value = "Select sum(debt) as totalOurMoney from supplier where debt > 0 and business_id = :businessId")
    Double allYourMoney(UUID businessId);
}
