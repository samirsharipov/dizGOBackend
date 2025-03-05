package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormDto {
    private List<UUID> lidFieldIds;

    private List<UUID> sourceId;

    private UUID businessId;

    private Double totalSumma;
}
