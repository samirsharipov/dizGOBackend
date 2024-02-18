package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTOS {
    private String projectName;
    private int projectTaskCount;
    private double tasksAmount;
    private double projectAmount;
}
