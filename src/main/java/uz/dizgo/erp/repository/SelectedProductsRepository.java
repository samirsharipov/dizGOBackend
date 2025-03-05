package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.SelectedProducts;

import java.util.List;
import java.util.UUID;

public interface SelectedProductsRepository extends JpaRepository<SelectedProducts, UUID> {
    List<SelectedProducts> findAllByBranchId(UUID branchId);
}
