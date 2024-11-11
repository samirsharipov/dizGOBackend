package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductGetDto {
    private UUID id;

    private Timestamp createdAt;

    private String invoice;

    private String productName;

    private int quantity;

    private String reasonName;

    private String reasonText;

    private double refundAmount;

    private boolean isMonetaryRefund;
}
