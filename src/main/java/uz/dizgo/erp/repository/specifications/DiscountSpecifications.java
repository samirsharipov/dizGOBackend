package uz.dizgo.erp.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import uz.dizgo.erp.entity.Discount;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.enums.DiscountType;

import javax.persistence.criteria.Join;
import java.sql.Timestamp;
import java.util.UUID;

public class DiscountSpecifications {

    public static Specification<Discount> belongsToBusiness(UUID businessId) {
        return (root, query, cb) -> {
            query.distinct(true); // DUBL NATIJALARNI OLDINI OLADI
            Join<Discount, Branch> branchJoin = root.join("branches");
            return cb.equal(branchJoin.get("business").get("id"), businessId);
        };
    }

    public static Specification<Discount> belongsToBranch(UUID branchId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Join<Discount, Branch> branchJoin = root.join("branches");
            return cb.equal(branchJoin.get("id"), branchId);
        };
    }

    public static Specification<Discount> hasType(DiscountType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Discount> startDateAfter(Timestamp startDate) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startDate"), startDate);
    }

    public static Specification<Discount> endDateBefore(Timestamp endDate) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("endDate"), endDate);
    }

    public static Specification<Discount> isActiveOrNotDeletedSortedByCreatedAtDesc() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(root.get("createdAt"))); // 🆕 DESC tartibda saralash
            return cb.equal(root.get("deleted"), false);
        };
    }
}
