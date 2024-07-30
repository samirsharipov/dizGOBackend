package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSupplierDto {
    private UUID id;
    private UUID customerId;
    private UUID supplierId;
    private String name;
    private double balance;
}
