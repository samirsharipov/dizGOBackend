package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementDto {
    private String name;
    private Double value;
    private UUID parentId;
    private UUID businessId;
    private List<MeasurementTranslateDto> translations;
}