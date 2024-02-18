package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String password;

    private UUID jobId;

    private String phoneNumber;

    private boolean sex;

    private Date birthday;

    private UUID roleId;

    private String roleName;

    private UUID businessId;

    private Set<UUID> branchId;

    private Set<BranchGetDto> branches;

    private UUID photoId;

    private boolean active;

    private String address;

    private String description;

    private Date probation;

    private String workingTime;

    private double salary;

    private List<UUID> bonusesId;

    @NotNull
    private String arrivalTime;

    @NotNull
    private String leaveTime;

    private boolean enabled;
}
