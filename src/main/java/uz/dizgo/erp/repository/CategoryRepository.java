package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findAllByBusiness_IdAndParentCategoryIsNull(UUID id);

    Optional<Category> findByBusiness_IdAndName(UUID business_id, String name);

    List<Category> findAllByParentCategory_Id(UUID parentCategoryId);

    List<Category> findAllByParentCategory_IdAndBusiness_Id(UUID parentId, UUID businessId);

    Optional<Category> findByName(String categoryName);

    List<Category> findByBusiness_Id(UUID businessId);
}
