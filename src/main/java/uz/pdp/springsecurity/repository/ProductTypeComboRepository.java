package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ProductTypeCombo;

import java.util.List;
import java.util.UUID;

public interface ProductTypeComboRepository extends JpaRepository<ProductTypeCombo, UUID> {
    List<ProductTypeCombo> findAllByMainProductId(UUID id);
}
