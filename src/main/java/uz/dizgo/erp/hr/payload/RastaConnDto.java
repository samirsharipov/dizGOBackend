package uz.dizgo.erp.hr.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class RastaConnDto {
    private UUID rasta;
    private ProductSelectSearchResult product;
}
