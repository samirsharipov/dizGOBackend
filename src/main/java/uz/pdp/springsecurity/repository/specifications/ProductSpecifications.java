package uz.pdp.springsecurity.repository.specifications;

import org.springframework.data.jpa.domain.Specification;
import uz.pdp.springsecurity.entity.Product;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.UUID;

public class ProductSpecifications {

    public static Specification<Product> isActiveTrue() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(root.get("active"));
    }

    public static Specification<Product> belongsToBranch(UUID branchId) {
        return (root, query, criteriaBuilder) -> {
            Join<Object, Object> branchJoin = root.join("branch", JoinType.LEFT);
            return criteriaBuilder.equal(branchJoin.get("id"), branchId);
        };
    }

    public static Specification<Product> belongsToBusiness(UUID businessId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("business").get("id"), businessId);
    }

    public static Specification<Product> nameOrBarcodeContains(String search) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("barcode")), "%" + search.toLowerCase() + "%")
        );
    }

    public static Specification<Product> belongsToCategory(UUID categoryId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> belongsToBrand(UUID brandId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("brand").get("id"), brandId);
    }
}
