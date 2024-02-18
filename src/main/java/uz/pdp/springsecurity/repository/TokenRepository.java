package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.Token;

import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Integer> {
}