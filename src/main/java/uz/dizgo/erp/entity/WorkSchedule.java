package uz.dizgo.erp.entity;

import lombok.*;
import uz.dizgo.erp.entity.template.AbsEntity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkSchedule extends AbsEntity {

    @JoinColumn(unique = true)
    private UUID userId;

    @Column(nullable = false)
    private LocalTime startTime; // kelish vaqti

    @Column(nullable = false)
    private LocalTime endTime; // ketish vaqti

    @Column(nullable = false)
    private LocalTime breakStart; // tanafus vaqti

    @Column(nullable = false)
    private LocalTime breakEnd; // tanafus tugash vaqti

    @Column(nullable = false)
    private String workDays; // Haftaning ishlaydigan kunlari (1-7 formatida)
}
