package uz.pdp.springsecurity.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Business;

import java.sql.Timestamp;
import java.time.LocalDate;
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

    @Query("SELECT COUNT(b) FROM Business b WHERE b.status = 'active' AND b.createdAt BETWEEN :startDate AND :endDate")
    long countActiveBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(b) FROM Business b WHERE b.status = 'blocked' and b.createdAt between :startDate and :endDate")
    long countBlockedBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(b) FROM Business b WHERE b.status = 'archive' and b.createdAt between :startDate and :endDate")
    long countArchivedBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(b) FROM Business b WHERE b.status is null and b.createdAt between :startDate and :endDate")
    long countNonActiveBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
