package uz.dizgo.erp.payload.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarListResult {
    private UUID id;
    private String driver;
    private String model;
    private String color;
    private String number;
    private Double price;
    private String file;
    private Double amount;
}
