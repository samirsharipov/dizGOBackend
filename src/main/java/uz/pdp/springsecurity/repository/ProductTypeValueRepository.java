package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ProductTypeValue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeValueRepository extends JpaRepository<ProductTypeValue, UUID> {
    List<ProductTypeValue> findAllByProductTypeId(UUID productType_id);

    Optional<ProductTypeValue> findAllByProductType_BusinessIdAndName(UUID productType_business_id, String name);



}
