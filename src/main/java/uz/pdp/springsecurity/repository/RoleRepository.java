package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.enums.Permissions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    boolean existsByNameIgnoreCaseAndBusinessId(String name, UUID businessId);
    boolean existsByNameIgnoreCaseAndBusinessIdAndIdIsNot(String name, UUID businessId,UUID id);
    Optional<Role> findByName(String name);
    Optional<Role> findByNameAndBusinessId(String name, UUID businessId);
    List<Role> findAllByBusiness_IdAndNameIsNot(UUID business_id, String name);
    void deleteAllByBusiness_Id(UUID id);
    List<Role> findAllByBusinessId(UUID business_id);
    List<Role> findAllByName(String name);
}
