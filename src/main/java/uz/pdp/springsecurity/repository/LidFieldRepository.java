package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.LidField;
import uz.pdp.springsecurity.enums.ValueType;

import java.util.List;
import java.util.UUID;

public interface LidFieldRepository extends JpaRepository<LidField, UUID> {
    List<LidField> findAllByBusiness_Id(UUID business_id);
    List<LidField> findAllByBusinessIsNull();
    List<LidField> findAllByBusinessIdAndValueType(UUID business_id, ValueType valueType);
}
