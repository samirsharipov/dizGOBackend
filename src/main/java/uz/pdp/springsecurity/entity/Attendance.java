package uz.pdp.springsecurity.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.pdp.springsecurity.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.sql.Timestamp;
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
    private Boolean isLeftEarly; // Vaqtidan oldin chiqib ketish

    @Column
    private String checkInPhotoUrl; // Geo-fencing orqali rasm URL

    @Column(nullable = false)
    private Boolean isArrived = true; // ishga keldi yoki kelmadi

    private Double incomeDistance = 0.0; // kirish radiusi

    private Double outcomeDistance = 0.0; // chiqish radiusi
}
