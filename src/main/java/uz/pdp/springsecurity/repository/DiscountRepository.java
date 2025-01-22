package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Discount;

import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
}
