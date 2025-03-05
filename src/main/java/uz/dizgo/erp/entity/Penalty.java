package uz.dizgo.erp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Penalty extends AbsEntity {
    private UUID employeeId;

    private UUID penaltyTemplateId; // Qaysi shablon asosida qo‘llanilgan

    private BigDecimal perMinutePenalty; // Har daqiqa uchun jarima miqdori

    private int minutesLate; // Kechikkan daqiqalar

    private BigDecimal totalPenalty; // Umumiy jarima summasi


    private boolean isCancelled; // Jarima bekor qilinganmi

    private String cancellationReason; // Bekor qilish sababi (agar bo‘lsa)
}
