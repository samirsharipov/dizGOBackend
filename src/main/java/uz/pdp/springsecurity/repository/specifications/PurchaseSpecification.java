package uz.pdp.springsecurity.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import uz.pdp.springsecurity.entity.Purchase;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PurchaseSpecification {
    public static Specification<Purchase> filterPurchases(UUID businessId, UUID userId, UUID supplierId, Timestamp startDate, Timestamp endDate, String status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (businessId != null) {
                predicates.add(criteriaBuilder.equal(root.get("branch").get("business").get("id"), businessId));
            }
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("seller").get("id"), userId));
            }
            if (supplierId != null) {
                predicates.add(criteriaBuilder.equal(root.get("supplier").get("id"), supplierId));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("purchaseStatus").get("status"), status));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
