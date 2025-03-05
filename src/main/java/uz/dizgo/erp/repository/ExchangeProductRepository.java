package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.ExchangeProduct;

import java.util.UUID;

public interface ExchangeProductRepository extends JpaRepository<ExchangeProduct, UUID> {

}
