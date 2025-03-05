package uz.dizgo.erp.hr.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class WarehouseDto {
    private UUID id;
    private String name;
}
