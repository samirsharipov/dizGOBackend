package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchGetDto {
    private UUID id;
    private String name;
    private UUID addressId;
    private UUID branchCategoryId;
    private String branchCategoryName;
    private String addressName;
    private double latitude;
    private double longitude;
}
