package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnProductDto {
    private UUID id;

    private String invoice;

    private UUID productId;

    private UUID tradeProductId;

    private int quantity;

    private UUID reasonId;

    private String reasonText;

    private double refundAmount;

    private boolean refunded;
}
