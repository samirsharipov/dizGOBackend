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
public class ProductionTaskDto {
    @NotNull
    private UUID taskId;
    @NotNull
    private UUID taskStatusId;

    @NotNull
    private Date date;

    @NotNull
    private double invalid;

    List<ContentProductDto> lossContentProductDtoList;
}
