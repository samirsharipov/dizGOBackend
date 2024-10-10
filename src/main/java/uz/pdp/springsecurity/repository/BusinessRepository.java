package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Business;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Business> findByName(String name);

    List<Business> findAllByDeleteIsFalse();
    @Query(nativeQuery = true, value = "SELECT DISTINCT b.*\n" +
            "FROM business b\n" +
            "         JOIN users u ON u.business_id = b.id\n" +
            "WHERE u.id = :userId ")
    List<Business> findAllByDeleteIsFalseMyFunc(UUID userId);
    Integer countAllByCreatedAtAfter(Timestamp startTime);

    @Query("SELECT b.id FROM Business b")
    List<UUID> findAllBusinessIds();
}
