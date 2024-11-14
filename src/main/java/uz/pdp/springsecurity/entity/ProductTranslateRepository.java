package uz.pdp.springsecurity.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductTranslateRepository extends JpaRepository<ProductTranslate, UUID> {
    void deleteAllByProductId(UUID productId);
}