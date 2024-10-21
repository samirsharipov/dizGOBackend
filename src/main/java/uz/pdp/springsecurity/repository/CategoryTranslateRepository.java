package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.CategoryTranslate;

import java.util.Optional;
import java.util.UUID;

public interface CategoryTranslateRepository extends JpaRepository<CategoryTranslate, UUID> {

    void deleteAllByCategory_Id(UUID categoryId);

    Optional<CategoryTranslate> findByCategory_IdAndLanguage_Code(UUID categoryId, String languageCode);
}
