package uz.dizgo.erp.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import uz.dizgo.erp.entity.Purchase;

import java.sql.Timestamp;
import java.util.UUID;

public class PurchaseSpecification {

    public static Specification<Purchase> belongsToBusiness(UUID businessId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("branch").get("business").get("id"), businessId);
    }

    public static Specification<Purchase> belongsToBranch(UUID branchId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("branch").get("id"), branchId);
    }

    public static Specification<Purchase> belongsToUser(UUID userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("seller").get("id"), userId);
    }

    public static Specification<Purchase> belongsToSupplier(UUID supplierId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("supplier").get("id"), supplierId);
    }

    public static Specification<Purchase> hasCreatedAtBetween(Timestamp startDate, Timestamp endDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdAt"), startDate, endDate);
    }

    public static Specification<Purchase> hasPurchaseStatus(String statusName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("paymentStatus").get("status"), statusName);
    }

    public static Specification<Purchase> hasDebtGreaterThanZero() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(root.get("debtSum"), 0);
    }

    public static Specification<Purchase> orderByCreatedAtDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return criteriaBuilder.conjunction();
        };
    }
}