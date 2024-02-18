package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Currency;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency,UUID> {
    Optional<Currency> findByBusinessId(UUID businessId);

    Optional<Currency> findFirstByCourseIsNotNullOrderByUpdateAtDesc();

    /*List<Currency> findAllByBusinessId(UUID business_id);

    List<Currency> findAllByBusinessIdAndActiveTrue(UUID business_id);

    Currency findByBusinessIdAndActiveTrue(UUID business_id);

    long countByName(String name);*/
}
