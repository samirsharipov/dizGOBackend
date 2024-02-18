package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Source;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SourceRepository extends JpaRepository<Source, UUID> {

    List<Source> findAllByBusiness_Id(UUID business_id);

    List<Source> findAllByBusinessIsNull();

    Optional<Source> findByName(String name);
    Optional<Source> findByNameAndBusinessId(String name, UUID business_id);
}
