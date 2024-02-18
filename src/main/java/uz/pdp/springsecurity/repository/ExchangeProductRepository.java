package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ExchangeProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExchangeProductRepository extends JpaRepository<ExchangeProduct, UUID> {

    Optional<ExchangeProduct> findByProductId(UUID product_id);

}
