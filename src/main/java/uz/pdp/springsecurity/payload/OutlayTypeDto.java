package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.OUTLAY_STATUS;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutlayTypeDto {
    private String name;
    private UUID paymentId;
    private UUID branchId;
    private boolean dollar;
    private OUTLAY_STATUS status;
    private List<OutlayTypeProduct> products = new ArrayList<>();
}
