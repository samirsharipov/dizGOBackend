package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.VerificationCode;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {

    Optional<VerificationCode> findByPhoneNumberAndCodeAndVerifiedFalse(String phoneNumber, String code);

    Optional<VerificationCode> findByPhoneNumber(String phoneNumber);

    Optional<VerificationCode> findByCodeAndSuperAdminTrue(String code);
}
