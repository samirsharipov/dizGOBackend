package uz.dizgo.erp.entity;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class EmergencyContact {
    private String name; // Aloqa uchun shaxsning ismi
    private String phoneNumber; // Aloqa uchun telefon raqami
}
