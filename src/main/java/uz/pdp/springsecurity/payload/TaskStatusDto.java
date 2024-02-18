package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskStatusDto {
    private UUID id;
    private String name;
    private String color;
    private long rowNumber;
    private boolean aBoolean;
    private UUID branchId;
}
