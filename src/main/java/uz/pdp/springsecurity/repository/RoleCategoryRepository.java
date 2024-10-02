package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.RoleCategory;

import java.util.List;
import java.util.UUID;

public interface RoleCategoryRepository extends JpaRepository<RoleCategory, UUID> {

    List<RoleCategory> findAllByBusinessId(UUID businessId);
}