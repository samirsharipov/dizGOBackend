package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDto {
    private String name;
    private UUID addressId;
    private String addressName;
    private double latitude;
    private double longitude;
    private UUID businessId;
    private UUID mainBranchId;
    private UUID categoryId;
}
