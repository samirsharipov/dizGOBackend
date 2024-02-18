package uz.pdp.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.springsecurity.entity.CustomerTelegramUser;

import java.util.UUID;

public interface CustomerTelegramUserRepository extends JpaRepository<CustomerTelegramUser, UUID> {
    CustomerTelegramUser findByChatId(Long chatId);
}
