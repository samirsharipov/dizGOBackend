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

import java.sql.Timestamp;
import java.util.*;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsById(UUID id);

    User findByChatId(Long chatId);

    boolean existsByUsernameIgnoreCase(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByPhoneNumber(String phoneNumber);

    List<User> findAllByRole_Id(UUID role_id);

    List<User> findAllByRole_IdAndBusiness_Delete(UUID role_id, boolean delete);

    List<User> findAllByBusiness_Id(UUID business_id);

    List<User> findAllByBusiness_IdAndRoleId(UUID business_id, UUID role_id);

    List<User> findAllByBranches_Id(UUID branches_id);

    List<User> findAllByBusiness_IdAndRoleIsNotAndActiveIsTrue(UUID business_id, Role role);

    List<User> findAllByBranchesIdAndRoleIsNotAndActiveIsTrue(UUID branches_id, Role role);

    Optional<User> findByBusinessIdAndRoleName(UUID business_id, String role_name);

    Page<User> findAllByFirstNameContainingIgnoreCaseAndBranchesIdAndUsernameNot(String firstName, UUID branches_id, String username, Pageable pageable);

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
}