package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.QRData;

public interface QRDataRepository extends JpaRepository<QRData, Long> {
}
