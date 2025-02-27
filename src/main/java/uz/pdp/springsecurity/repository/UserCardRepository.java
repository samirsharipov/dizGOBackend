package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.UserCard;

import java.util.Optional;
import java.util.UUID;

public interface UserCardRepository extends JpaRepository<UserCard, UUID> {
    Optional<UserCard> findByUserIdAndCardNumber(UUID userId, String cardNumber);
}
