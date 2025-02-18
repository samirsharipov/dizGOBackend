package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.enums.Permissions;
import uz.pdp.springsecurity.payload.MonthlyEmploymentDismissalReportDto;
import uz.pdp.springsecurity.payload.projections.UserProjection;

import java.sql.Timestamp;
import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsById(UUID id);

    boolean existsByUsernameIgnoreCase(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAllByRole_Id(UUID role_id);

    List<User> findAllByRole_IdAndBusiness_Deleted(UUID role_id, boolean delete);

    List<User> findAllByBusiness_Id(UUID business_id);

    List<User> findAllByBusiness_IdAndRoleId(UUID business_id, UUID role_id);

    List<User> findAllByBranches_Id(UUID branches_id);

    Page<User> findAllByBusinessId(UUID business_id, Pageable pageable);

    @Query("SELECT u FROM users u WHERE u.business.id = :businessId " +
            "AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    Page<User> findByBusinessIdAndSearchTerm(@Param("businessId") UUID businessId,
                                             @Param("searchTerm") String searchTerm,
                                             Pageable pageable);

    List<User> findAllByBranchesIdAndRoleIsNotAndActiveIsTrue(UUID branches_id, Role role);

    Optional<User> findByBusinessIdAndRoleName(UUID business_id, String role_name);

    List<User> findAllByBusiness_IdAndRoleName(UUID business_id, String role_name);

    List<User> findAllByJobId(UUID id);

    List<User> findAllByIdIn(Collection<UUID> id);

    List<User> findAllByBusiness_IdAndBirthdayBetween(UUID business_id, Date startDate, Date endDate);

    int countAllByBranchesIdAndRole_Permissions(UUID branchId, Permissions permissions);

    int countAllByBranchesId(UUID branchId);

    int countAllByBranches_Business_IdAndActiveIsTrue(UUID branches_business_id);

    List<User> findAllByUsernameContainingIgnoreCase(String username);

    @Query(value = "SELECT * from users u WHERE u.business_id = :businessId AND u.id <> :id ORDER BY created_at", nativeQuery = true)
    List<User> findAllByBusiness_IdAndId(UUID businessId, UUID id);


    @Query("SELECT new uz.pdp.springsecurity.payload.MonthlyEmploymentDismissalReportDto" +
            "(COUNT(u.id), " +
            "(SELECT COUNT(d.id) FROM Dismissal d WHERE d.user.business.id = :businessId " +
            "AND d.createdAt BETWEEN :startDate AND :endDate)) " +
            "FROM users u WHERE u.business.id = :businessId " +
            "AND u.active = true AND u.enabled = true " +
            "AND u.createdAt BETWEEN :startDate AND :endDate")
    List<MonthlyEmploymentDismissalReportDto> getMonthlyReportByBusinessId(
            @Param("businessId") UUID businessId,
            @Param("startDate") Timestamp startDate,
            @Param("endDate") Timestamp endDate);

    int countByBusiness_Id(UUID businessId);

    @Query("select count(u) from users u where u.createdAt between :startDate and :endDate")
    long countTotalBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    boolean existsByPhoneNumber(String phoneNumber);

    Page<User> findAllByFirstNameRegexAndBranchesIdAndUsernameNot(String queryName, UUID branchId, String superadmin, Pageable pageable);

    @Query("select count(u.id) from users u " +
            "join u.branches b " +
            "where b.id = :branchId and u.active = true and u.deleted = false")
    Long countActiveUsersByBranch(@Param("branchId") UUID branchId);

    @Query("select count(u.id) from users u " +
            "where u.business.id = :businessId and u.active = true and u.deleted = false")
    Long countActiveUsersByBusiness(@Param("businessId") UUID businessId);


    Optional<User> findByRoleName(String role);

    @Query("SELECT new uz.pdp.springsecurity.payload.projections.UserProjection(u.id, CONCAT(u.firstName, ' ', u.lastName)) " +
            "FROM users u " +
            "WHERE u.business.id = :businessId ")
    List<UserProjection> findUsersByBusiness(@Param("businessId") UUID businessId);

    @Query("SELECT new uz.pdp.springsecurity.payload.projections.UserProjection(u.id, CONCAT(u.firstName, ' ', u.lastName)) " +
            "FROM users u " +
            "join u.branches b " +
            "WHERE b.id = :branchId ")
    List<UserProjection> findUsersByBranch(@Param("branchId") UUID branchId);
}