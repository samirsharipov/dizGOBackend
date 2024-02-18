package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfoGetDto {
    private Integer taskAmount;
    private Integer doneTaskAmount;
    private Integer notDoneDeadlineAmount;
}
