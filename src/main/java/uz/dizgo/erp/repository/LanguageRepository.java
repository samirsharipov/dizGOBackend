package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Language;

import java.util.Optional;
import java.util.UUID;

public interface LanguageRepository extends JpaRepository<Language, UUID> {

    Optional<Language> findByCode(String languageCode);
}
