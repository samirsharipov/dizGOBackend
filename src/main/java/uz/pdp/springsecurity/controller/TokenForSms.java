package uz.pdp.springsecurity.controller;

import lombok.*;
import uz.pdp.springsecurity.entity.template.AbsEntity;

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
