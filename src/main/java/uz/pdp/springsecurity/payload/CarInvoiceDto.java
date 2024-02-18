package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.CarInvoiceType;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInvoiceDto {
    private UUID id;
    private Double amount;
    private CarInvoiceType type;
    private String description;
    private UUID branch;
    private UUID pay;
    private String mileage;
}
