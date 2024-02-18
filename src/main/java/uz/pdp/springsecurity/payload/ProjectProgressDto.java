package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectProgressDto {
    private int totalTask;
    private int completedTask;
    private int percent;
    private Date startDate;
    private Date deadline;
    private Date approximateDate;
    private int goalDay;
    private int leftDay;
    private int remainDay;
    private int LateDay;
}
