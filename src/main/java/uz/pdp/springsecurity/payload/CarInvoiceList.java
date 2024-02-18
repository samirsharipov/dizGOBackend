package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.CarInvoiceType;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarInvoiceList {
    private UUID id;
    private CarInvoiceType type;
    private Date date;
    private Double amount;
    private String description;
    private String payType;
    private String branch;
    private String mileage;
}
