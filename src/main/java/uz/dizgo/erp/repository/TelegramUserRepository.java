package uz.dizgo.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.dizgo.erp.entity.TelegramUser;

import java.util.UUID;

public interface TelegramUserRepository extends JpaRepository<TelegramUser, UUID> {
    TelegramUser findByChatId(Long chatId);
}
