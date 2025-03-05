package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierGetDto {
    private UUID id;

    private String name;

    private String phoneNumber;

    private boolean juridical;

    private String inn;

    private UUID businessId;

    private double debt;
}
