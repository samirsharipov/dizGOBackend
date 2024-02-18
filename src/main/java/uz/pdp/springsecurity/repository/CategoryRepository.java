package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findAllByBusiness_Id(UUID business_id);
    Optional<Category> findAllByBusiness_IdAndName(UUID business_id,String name);
    List<Category> findCategoriesByParentCategoryId(UUID parentCategory_id);
    List<Category> findAllByBusiness_IdAndAndParentCategoryNull(UUID businessId);
    List<Category> findAllByBusinessIdAndParentCategoryNotNull(UUID business_id);


}
