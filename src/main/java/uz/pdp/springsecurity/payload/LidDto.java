package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LidDto {
    private UUID id;
    private Map<UUID, String> values;
    private UUID lidStatusId;
    private UUID businessId;
    private UUID formId;
}
