package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementTranslateGetDto {
    private UUID id;

    private String name;

    private String description;

    private UUID languageId;

    private String code;
}
