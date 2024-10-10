package uz.pdp.springsecurity.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyEmploymentDismissalReportDto {
    private long activeUsersCount; // Ishlayotganlar soni
    private long dismissedUsersCount; // Boshatilganlar soni
}
