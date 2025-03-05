package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.LidField;
import uz.dizgo.erp.enums.ValueType;

import java.util.List;
import java.util.UUID;

public interface LidFieldRepository extends JpaRepository<LidField, UUID> {
    List<LidField> findAllByBusiness_Id(UUID business_id);

    List<LidField> findAllByBusinessIsNull();

    List<LidField> findAllByBusinessIdAndValueType(UUID business_id, ValueType valueType);
}
