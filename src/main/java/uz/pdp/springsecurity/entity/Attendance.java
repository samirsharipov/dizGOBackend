package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attendance extends AbsEntity {

    private UUID employeeId;

    @Column(nullable = false)
    private Timestamp checkInTime; // Kirish vaqti

    @Column
    private Timestamp checkOutTime; // Chiqish vaqti

    @Column
    private Boolean isLate; // Kechikish bo'ldi yoki yo'q

    @Column
    private Long lateMinutes = 0L; // Kechikkan daqiqalar

    @Column
    private Boolean isLeftEarly; // Vaqtidan oldin chiqib ketish

    @Column
    private String checkInPhotoUrl; // Geo-fencing orqali rasm URL

    @Column(nullable = false)
    private Boolean isArrived = true; // ishga keldi yoki kelmadi

    private Double incomeDistance = 0.0; // kirish radiusi

    private Double outcomeDistance = 0.0; // chiqish radiusi

    private UUID branchId;

    @Column
    private Long workDuration; // Ishlagan vaqt (sekundlarda)

    @PreUpdate
    void calculateWorkDuration() {
        if (checkInTime != null && checkOutTime != null) {
            this.workDuration = (checkOutTime.getTime() - checkInTime.getTime()) / 1000; // Sekundlarda hisoblash
        }
    }

    public Long getDuration() {
        if (checkInTime == null) {
            return null; // Agar check-in bo‘lmagan bo‘lsa, work duration null qaytariladi
        }

        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        Timestamp effectiveCheckOutTime = (checkOutTime != null) ? checkOutTime : currentTime;

        return (effectiveCheckOutTime.getTime() - checkInTime.getTime()) / 1000; // Sekundlarda hisoblash
    }
}
