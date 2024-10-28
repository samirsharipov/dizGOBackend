package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.STATE;

import javax.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bot_users")
public class TelegramUser extends AbsEntity {
    @Column(nullable = false, unique = true)
    private Long chatId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private STATE state;
    private String username;

    public TelegramUser(Long chatId, STATE state) {
        this.chatId = chatId;
        this.state = state;
    }
}
