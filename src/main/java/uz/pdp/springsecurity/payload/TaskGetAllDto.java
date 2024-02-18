package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskGetAllDto {
    private String name;
    private String projectName;
    private String description;
    private List<UUID> fileDataId;
    private Date endDate;
    private String taskStatus;
    private List<UserDtos> userDtosList;

}
