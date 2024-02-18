package uz.pdp.springsecurity.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TokenForSms extends AbsEntity {
    @Column(columnDefinition = "TEXT")
    private String token;
}
