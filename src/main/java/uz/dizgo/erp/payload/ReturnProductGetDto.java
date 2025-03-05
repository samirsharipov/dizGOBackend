package uz.dizgo.erp.payload;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductGetDto {
    private UUID id;

    private Date createdAt;

    private String invoice;

    private String productName;

    private int quantity;

    private String reasonName;

    private String reasonText;

    private double refundAmount;

    private boolean isMonetaryRefund;
}
