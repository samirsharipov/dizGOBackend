package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.CustomerTelegramUser;

import java.util.UUID;

public interface CustomerTelegramUserRepository extends JpaRepository<CustomerTelegramUser, UUID> {
    CustomerTelegramUser findByChatId(Long chatId);
}
