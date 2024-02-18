package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.CustomerGroup;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerGroupRepository extends JpaRepository<CustomerGroup, UUID> {
    List<CustomerGroup> findAllByBusiness_Id(UUID business_id);
}
