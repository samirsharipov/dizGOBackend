package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;
import uz.pdp.springsecurity.enums.STATE;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_bot_users")
public class CustomerTelegramUser extends AbsEntity {
    @Column(nullable = false, unique = true)
    private Long chatId;

    public CustomerTelegramUser(Long chatId, STATE state) {
        this.chatId = chatId;
        this.state = state;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private STATE state;
    private Integer messageId;
}
