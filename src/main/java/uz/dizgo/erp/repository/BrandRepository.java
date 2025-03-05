package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Brand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BrandRepository extends JpaRepository<Brand, UUID> {
    List<Brand> findAllByBusiness_Id(UUID branch_id);

    Optional<Brand> findByBusiness_IdAndName(UUID business_id, String name);

    Optional<Brand> findByName(String brandName);

    List<Brand> findByBusiness_Id(UUID businessId);
}
