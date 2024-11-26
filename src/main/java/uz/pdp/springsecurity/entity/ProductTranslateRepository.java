package uz.pdp.springsecurity.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTranslateRepository extends JpaRepository<ProductTranslate, UUID> {

    void deleteAllByProductId(UUID productId);

    Optional<ProductTranslate> findByProductIdAndLanguage_Id(UUID product_id, UUID languageId);

    List<ProductTranslate> findAllByProductId(UUID productId);
}