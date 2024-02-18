package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    @NotNull(message = "required line")
    private String firstName;
    @NotNull(message = "required line")
    private String lastName;
    @NotNull(message = "required line")
    private String username;
    @NotNull(message = "required line")
    private String email;
    @NotNull(message = "required line")
    private String password;
    @NotNull(message = "required line")
    private String prePassword;

    private UUID photoId;
}
