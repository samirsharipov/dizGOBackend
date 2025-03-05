package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PenaltyTemplate extends AbsEntity {

    private String name; // Jarima shablonining nomi, masalan, "Kechikish jarimasi"

    private UUID branchId; // Qaysi filialga tegishli ekanligi

    private BigDecimal perMinutePenalty; // Har daqiqa uchun jarima summasi
}