package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Integer> {
}