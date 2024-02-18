package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Bonus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BonusRepository extends JpaRepository<Bonus, UUID> {
    Optional<Bonus> findByDeleteFalseAndId(UUID id);

    List<Bonus> findAllByBusinessIdAndDeleteFalse(UUID businessId);

    boolean existsByNameIgnoreCaseAndBusinessIdAndDeleteFalse(String name, UUID businessId);

    boolean existsByNameIgnoreCaseAndBusinessIdAndIdIsNotAndDeleteFalse(String name, UUID businessId, UUID id);
}
