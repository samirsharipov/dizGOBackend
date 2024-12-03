package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.VerificationCode;

import java.util.Optional;
import java.util.UUID;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID> {
    Optional<VerificationCode> findByPhoneNumberAndCodeAndVerifiedFalse(String phoneNumber, String code);

    Optional<VerificationCode> findByPhoneNumberAndVerifiedFalse(String phoneNumber);
}
