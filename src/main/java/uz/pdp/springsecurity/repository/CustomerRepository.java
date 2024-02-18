package uz.pdp.springsecurity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.pdp.springsecurity.entity.Customer;
import uz.pdp.springsecurity.payload.projections.InActiveUserProjection;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> findAllByBusiness_IdAndActiveIsTrueOrBusiness_IdAndActiveIsNull(UUID business_id, UUID business_id2);

    Customer findByChatId(Long chatId);

    List<Customer> findAllByCustomerGroupIdAndActiveIsTrueOrCustomerGroupIdAndActiveIsNull(UUID customerGroup_id, UUID customerGroup_id2);

    List<Customer> findAllByBusiness_IdAndBirthdayBetweenAndActiveIsTrueOrBusiness_IdAndBirthdayBetweenAndActiveIsNull(UUID business_id, Date birthday, Date birthday2, UUID business_id2, Date birthday3, Date birthday4);

    List<Customer> findAllByBusiness_IdAndDebtIsNotOrderByPayDateAsc(UUID business_id, Double debt);

    List<Customer> findAllByBranchesIdAndActiveIsTrueOrBranchesIdAndActiveIsNull(UUID branches_id, UUID branches_id2);

    List<Customer> findAllByBranchesIdAndDebtIsNotOrderByPayDateAsc(UUID branchId, Double debt);

    List<Customer> findAllByBusinessIdAndDebtIsNotOrderByPayDateAsc(UUID businessId, Double debt);

    Optional<Customer> findByPhoneNumberAndActiveIsTrueOrPhoneNumberAndActiveIsNull(String phoneNumber, String phoneNumber2);

    Customer findByPhoneNumber(String phoneNumber);

    List<Customer> findAllByPayDateBetweenAndBusinessIdAndActiveIsTrueOrPayDateBetweenAndBusinessIdAndActiveIsNull(Date payDate, Date payDate2, UUID business_id, Date payDate3, Date payDate4, UUID business_id2);

    List<Customer> findAllByBranchesIdAndLidCustomerIsTrueAndActiveIsTrueOrBranchesIdAndLidCustomerIsTrueAndActiveIsNull(UUID branches_id, UUID branches_id2);


    int countAllByBranchesId(UUID branchId);

    List<Customer> findAllByBranchId(UUID branch_id);

    int countAllByBusinessId(UUID business_id);

    List<Customer> findAllByBusinessId(UUID business_id);

    Page<InActiveUserProjection> findAllByBranchIdAndPayDateNotNullOrderByPayDateAsc(UUID branch_id, Pageable pageable);

    int countAllByCustomerGroupId(UUID customerGroup_id);

    Integer countAllByBranch_IdAndCreatedAtBetween(UUID branch_id, Timestamp createdAt, Timestamp createdAt2);

    List<Customer> findAllByBranch_IdAndCreatedAtBetween(UUID branch_id, Timestamp createdAt, Timestamp createdAt2);

    Page<Customer> findAllByBranch_IdAndActiveIsTrue(Pageable pageable, UUID branch_id);

    Integer countAllByBusiness_IdAndCreatedAtBetween(UUID businessId, Timestamp createdAt, Timestamp createdAt2);

    List<Customer> findAllByBranchIdAndNameContainingIgnoreCase(UUID branch_id, String name);

    List<Customer> findAllByBusiness_IdAndCreatedAtBetween(UUID businessId, Timestamp start, Timestamp end);

    @Query(nativeQuery = true, value = "Select sum(debt) as totalOurMoney from customer where debt < 0 and business_id = :businessId")
    Double allOurMoney(UUID businessId);

    @Query(nativeQuery = true, value = "Select sum(debt) as totalOurMoney from customer where debt > 0 and business_id = :businessId")
    Double allYourMoney(UUID businessId);
}
