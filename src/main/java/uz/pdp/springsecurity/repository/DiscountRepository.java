package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Discount;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    List<Discount> findAll(Specification<Discount> spec);
}
