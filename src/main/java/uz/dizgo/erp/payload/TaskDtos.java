package uz.dizgo.erp.payload;

import lombok.Data;

import java.util.Date;
import java.util.List;
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
