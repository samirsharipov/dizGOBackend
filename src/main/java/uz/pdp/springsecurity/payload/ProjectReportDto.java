package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectReportDto {

    private int projectQuantity;
    private double projectAmount;
    private List<ProjectDTOS> projectDTOSList;
    private double tasksAmount;
}
