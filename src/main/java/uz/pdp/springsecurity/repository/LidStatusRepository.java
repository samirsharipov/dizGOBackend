package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.LidStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LidStatusRepository extends JpaRepository<LidStatus, UUID> {
    List<LidStatus> findAllByBusiness_IdOrderBySortAsc(UUID business_id);

    List<LidStatus> findAllByBusinessIsNullOrderBySortAsc();

    boolean existsBySaleStatusIsTrue();

    Optional<LidStatus> findByName(String name);


    Optional<LidStatus> findBySortAndBusinessId(Integer sort, UUID business_id);

}
