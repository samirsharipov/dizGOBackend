package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.controller.TokenForSms;

import java.util.UUID;

public interface TokenForSmsRepository extends JpaRepository<TokenForSms, UUID> {

}
