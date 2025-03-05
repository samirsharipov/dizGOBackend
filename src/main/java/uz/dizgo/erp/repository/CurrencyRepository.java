package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Currency;

import java.util.Optional;
import java.util.UUID;

public interface CurrencyRepository extends JpaRepository<Currency,UUID> {
    Optional<Currency> findByBusinessId(UUID businessId);

    Optional<Currency> findFirstByCourseIsNotNullOrderByUpdateAtDesc();
}
