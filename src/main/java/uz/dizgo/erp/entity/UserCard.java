package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserCard extends AbsEntity {

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, unique = true)
    private Long cardId;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String expireDate;

    private String ownerName;

    private BigDecimal balance;

    private String status;
}