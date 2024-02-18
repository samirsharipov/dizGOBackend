package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Brand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
    List<Brand> findAllByBusiness_Id(UUID branch_id);
    Optional<Brand> findAllByBusiness_IdAndName(UUID business_id, String name);

//    List<Brand> findAllByBranchIdAndActiveTrue(UUID id);
//    List<Brand> findAllBy(UUID id);

}
