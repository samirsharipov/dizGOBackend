package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Form;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormRepository extends JpaRepository<Form, UUID> {
    List<Form> findAllByBusiness_Id(UUID business_id);

    List<Form> findAllByLidFieldsId(UUID lidFields_id);

    Optional<Form> findBySourceId(UUID source_id);
}
