package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.springsecurity.entity.Business;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StageDto {

    private String name;

    private UUID branchId;
}
