package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.SelectForLid;

import java.util.List;
import java.util.UUID;

public interface SelectForLidRepository extends JpaRepository<SelectForLid, UUID> {
    List<SelectForLid> findAllByLidField_BusinessId(UUID lid_business_id);
    List<SelectForLid> findAllByLidFieldId(UUID lid_id);

}
