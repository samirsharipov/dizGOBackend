package uz.pdp.springsecurity.payload;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductSearchCriteria {
    private UUID branchId;
    private UUID businessId;
    private UUID catId;
    private UUID brandId;
    private String search;
}
