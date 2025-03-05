package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Tax;

import java.util.List;
import java.util.UUID;

public interface TaxRepository extends JpaRepository<Tax, UUID> {

    List<Tax> findAllByBusiness_Id(UUID business_id);

    boolean existsByBusinessId(UUID business_id);
}
