package uz.dizgo.erp.controller;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TokenForSms extends AbsEntity {
    @Column(columnDefinition = "TEXT")
    private String token;
}
