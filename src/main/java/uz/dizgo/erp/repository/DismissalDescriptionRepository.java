package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.DismissalDescription;

import java.util.List;
import java.util.UUID;

public interface DismissalDescriptionRepository extends JpaRepository<DismissalDescription, UUID> {

    List<DismissalDescription> findAllByBusinessId(UUID businessId);

    List<DismissalDescription> findAllByBusinessIdAndMandatoryTrue(UUID businessId);

    List<DismissalDescription> findAllByBusinessIdAndMandatoryFalse(UUID businessId);
}
