package uz.pdp.springsecurity.payload;

import lombok.Data;
import uz.pdp.springsecurity.entity.LidStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class TaskDtos {

    private String name;
    private List<UserPhotoAndNameDto> userList;
    private UUID taskStatusId;
    private String taskStatusOrginalName;
    private List<UUID> userPhotoId;
    private Date date;
}
