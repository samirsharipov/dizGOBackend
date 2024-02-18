package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskGetOne {

    private String taskName;
    private String taskStatusName;
    private String taskStatusColor;
    private long percent;
    private Date deadline;
}
