package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessStat implements Serializable {

    private long total;
    private long activeCount;
    private long blockedCount;
    private long archivedCount;
    private long nonActiveCount;
}
