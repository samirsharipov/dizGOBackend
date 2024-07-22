package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.Permissions;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    private UUID id;
    @NotBlank
    private String name;

    private Boolean isOffice;

    @NotEmpty
    private List<Permissions> permissions;

    private String description;

    private UUID businessId;

    private UUID parentRole;

    private List<UserDataByRoleDto> users;
}
