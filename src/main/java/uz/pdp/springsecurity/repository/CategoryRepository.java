package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findAllByBusiness_IdAndParentCategoryIsNull(UUID id);

    Optional<Category> findAllByBusiness_IdAndName(UUID business_id, String name);

    List<Category> findAllByParentCategory_Id(UUID parentCategoryId);
}
