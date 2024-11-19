package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.MeasurementTranslate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MeasurementTranslateRepository extends JpaRepository<MeasurementTranslate, UUID> {
    void deleteByMeasurement_id(UUID measurement_id);
    Optional<MeasurementTranslate> findByMeasurement_idAndLanguage_Code(UUID measurement_id, String language_code);
    List<MeasurementTranslate> findByMeasurement_id(UUID measurement_id);
}
