package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.RoleCategory;

import java.util.List;
import java.util.UUID;

public interface RoleCategoryRepository extends JpaRepository<RoleCategory, UUID> {

    List<RoleCategory> findAllByBusinessId(UUID businessId);
}