package uz.pdp.springsecurity.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.Type;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentPlanProductResult {
    private UUID id;
    private String name;
}
