package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.QRData;

public interface QRDataRepository extends JpaRepository<QRData, Long> {
}
