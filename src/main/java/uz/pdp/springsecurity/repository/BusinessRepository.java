package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Business;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusinessRepository extends JpaRepository<Business, UUID> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByBusinessNumberIgnoreCase(String number);

    Optional<Business> findByName(String name);

    Optional<Business> findByBusinessNumber(String number);

    List<Business> findAllByDeletedIsFalse();

    List<Business> findAllByDeletedIsTrue();

    @Query(nativeQuery = true, value = "SELECT DISTINCT b.* FROM business b JOIN users u ON u.business_id = b.id WHERE u.id = :userId ")
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

    @Query("SELECT COUNT(b) FROM Business b WHERE b.createdAt between :startDate and :endDate")
    long countTotalBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT COUNT(s.business) " +
            "FROM Subscription s " +
            "WHERE s.tariff.name = :tariffName AND s.active = true and s.createdAt between :startDate and :endDate ")
    long countBusinessesByTariffName(@Param("tariffName") String tariffName, @Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}
