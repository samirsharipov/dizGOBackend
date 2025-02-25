package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.UserCard;

import java.util.UUID;

public interface UserCardRepository extends JpaRepository<UserCard, UUID> {
}
