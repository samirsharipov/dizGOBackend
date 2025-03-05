package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {
    private UUID id;
    private String driver;
    private String model;
    private String color;
    private String carNumber;
    private UUID businessId;
    private String file;
    private Double price;
}
