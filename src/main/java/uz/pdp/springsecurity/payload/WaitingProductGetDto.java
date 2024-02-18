package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitingProductGetDto {
    private UUID productId;

    private UUID productTypePriceId;

    private String type;

    private String productName;

    private String measurement;

    private String subMeasurementName;

    private double subMeasurementValue;

    private boolean subMeasurement;

    private double salePrice;

    private double quantity;

    private double totalPrice;

    private double amount;
}
