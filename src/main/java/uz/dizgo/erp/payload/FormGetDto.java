package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormGetDto {
    private UUID id;
    private List<LidFieldDto> lidFieldDtos;
    private SourceDto sourceDto;
    private UUID businessId;
}
