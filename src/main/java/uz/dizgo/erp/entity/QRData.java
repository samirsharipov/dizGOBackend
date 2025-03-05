package uz.dizgo.erp.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "qr_data")
public class QRData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qr_data_seq")
    @SequenceGenerator(name = "qr_data_seq", sequenceName = "qr_data_sequence", allocationSize = 1, initialValue = 1000000)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updateAt;
    @CreatedBy
    private UUID createdBy;
    @LastModifiedBy
    private UUID lastModifiedBy;

    private UUID branchId;
    private String branchName;
    private UUID userId;
    private String userName;
    private String ePosCode;
}