package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ReturnProduct;

import java.util.UUID;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, UUID> {
}
