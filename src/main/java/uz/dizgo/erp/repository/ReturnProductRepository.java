package uz.dizgo.erp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.dizgo.erp.entity.ReturnProduct;
import uz.dizgo.erp.payload.ReturnProductGetDto;

import java.util.UUID;

public interface ReturnProductRepository extends JpaRepository<ReturnProduct, UUID> {

    @Query("SELECT DISTINCT new uz.dizgo.erp.payload.ReturnProductGetDto(r.id, r.createdAt, r.invoice, p.name, r.quantity, r.reason.name, r.reasonText, r.refundAmount, r.isMonetaryRefund) " +
            "FROM ReturnProduct r " +
            "JOIN Product p ON p.id = r.productId " +
            "WHERE r.businessId = :businessId AND r.active = true")
    Page<ReturnProductGetDto> findByBusinessId(@Param("businessId") UUID businessId, Pageable pageable);

}
