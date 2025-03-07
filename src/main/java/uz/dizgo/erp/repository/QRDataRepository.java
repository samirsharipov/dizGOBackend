package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.QRData;

import java.util.List;
import java.util.UUID;

public interface QRDataRepository extends JpaRepository<QRData, Long> {
    List<QRData> findAllByBranchId(UUID branchId);
}
