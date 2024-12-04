package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.Permissions;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDto {

    @NotNull(message = "required line")
    private String name;

    private String businessNumber;

    private String addressHome;

    private UUID tariffId;

    private boolean isActive;

    private UserCreateDto userDto;

    private UUID branchCategoryId;

    private UUID addressId;

    private List<Permissions> permissionsList;
}
