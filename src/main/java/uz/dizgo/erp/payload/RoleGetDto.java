package uz.dizgo.erp.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleGetDto {

    private UUID id;

    private String name;

    private Boolean isOffice;

    private String description;

    private UUID businessId;

    private UUID parentRoleId;

    private UUID roleCategoryId;
}
