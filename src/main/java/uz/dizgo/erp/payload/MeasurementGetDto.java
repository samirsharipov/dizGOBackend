package uz.dizgo.erp.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class MeasurementGetDto {

    private UUID id;

    private String name;

    private Double value;

    private String description;

    private UUID businessId;

    private UUID parentMeasurementId;

    private String parentMeasurementName;
}
