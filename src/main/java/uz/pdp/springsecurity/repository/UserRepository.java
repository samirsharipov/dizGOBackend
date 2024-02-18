package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Role;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.enums.Permissions;

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

    List<User> findAllByUsernameContainingIgnoreCase(String username);
    @Query(value = "SELECT * from users u WHERE u.business_id = :businessId AND u.id <> :id ORDER BY created_at", nativeQuery = true)
    List<User> findAllByBusiness_IdAndId(UUID businessId, UUID id);

}