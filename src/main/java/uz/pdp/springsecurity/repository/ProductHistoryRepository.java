package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.ProductHistory;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public interface ProductHistoryRepository extends JpaRepository<ProductHistory, UUID> {
}
