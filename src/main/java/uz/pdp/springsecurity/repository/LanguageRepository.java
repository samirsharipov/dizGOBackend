package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Language;

import java.util.Optional;
import java.util.UUID;

public interface LanguageRepository extends JpaRepository<Language, UUID> {

    Optional<Language> findByCode(String languageCode);
}
