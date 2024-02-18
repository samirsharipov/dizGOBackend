package uz.pdp.springsecurity.payload;

import lombok.Data;
import uz.pdp.springsecurity.entity.FileData;
import uz.pdp.springsecurity.entity.TaskPrice;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class TaskDto {

    private UUID Id;
    @NotNull
    private String name;
    private UUID projectId;
    private UUID taskTypeId;
    private Date startDate;
    private List<TaskPriceDto> taskPriceDtos;
    private Date deadLine;
    private UUID taskStatus;
    private String importance;
    private UUID dependTask;
    private UUID stageId;
    private UUID contentId;
    private boolean isProductions;
    private double goalAmount;

    private boolean costEachOne;

    private double cost;

    private List<CostDto> costDtoList;
    List<ContentProductDto> contentProductDtoList;
    private double taskPrice;
    @NotNull
    private UUID branchId;

    private String description;

    private List<UUID> fileDataList;

}
