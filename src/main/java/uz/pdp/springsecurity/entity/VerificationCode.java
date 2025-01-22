package uz.pdp.springsecurity.entity;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode extends AbsEntity {

    private String code;

    @Column(unique = true)
    private String phoneNumber;

    private boolean verified = false;

    private LocalDateTime expiresAt;

    private Boolean superAdmin;

    public VerificationCode(String phoneNumber, String code, LocalDateTime localDateTime, boolean isSuperAdmin) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.expiresAt = localDateTime;
        this.superAdmin = isSuperAdmin;
    }
}
