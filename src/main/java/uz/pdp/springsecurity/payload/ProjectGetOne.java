package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectGetOne {

    private String projectName;
    private String projectTypeName;
    private double projectBudget;
    private int totalTask;
    private int completedTask;
    private int expiredTask;
    private double totalTaskSum;
    private String customerName;
    private int projectPercent;
    private Date startDate;
    private Date endDate;
    private Date deadline;
    private List<TaskGetOne> taskGetOneList;
    private List<FileDataDto> fileDataList;
    private List<StageProject> stageProjectList;
    private List<UserProject> userProjectList;
    private List<ContentProject> contentProjectList;

}
