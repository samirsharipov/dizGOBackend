package uz.pdp.springsecurity.payload.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProjection {
    private UUID id;
    private String fullName;
}
