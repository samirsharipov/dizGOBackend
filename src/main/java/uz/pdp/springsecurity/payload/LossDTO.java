package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LossDTO {
    @NotNull
    private UUID branchId;

    @NotNull
    private UUID userId;

    @NotNull
    private Date date;

    @NotNull
    List<LossProductDto> lossProductDtoList;
}
