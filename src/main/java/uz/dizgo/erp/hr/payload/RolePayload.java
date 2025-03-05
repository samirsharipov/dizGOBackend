package uz.dizgo.erp.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RolePayload {
    private UUID id;
    private String name;
}
