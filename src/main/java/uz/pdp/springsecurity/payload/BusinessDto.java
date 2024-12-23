package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.enums.Permissions;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDto {
    private UUID id;

    @NotNull(message = "required line")
    private String name;

    private String businessNumber;

    private String description;

    private String addressHome;

    private UUID tariffId;

    private boolean isActive;

    private UserCreateDto userDto;

    private UUID branchCategoryId;

    private UUID addressId;

    private Timestamp contractStartDate;

    private Timestamp contractEndDate;

    private List<Permissions> permissionsList;
}
