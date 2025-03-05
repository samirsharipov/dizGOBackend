package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Measurement;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {
    List<Measurement> findAllByBusiness_Id(UUID business_id);

    Optional<Measurement> findByBusinessIdAndName(UUID business_id, String name);

    List<Measurement> findByBusinessId(UUID businessId);
}
