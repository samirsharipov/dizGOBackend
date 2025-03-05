package uz.dizgo.erp.hr.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EmployeesPageTableResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String job;
    private String role;
    List<BonusPayload> bonuses;

    public EmployeesPageTableResponse(UUID id, String firstName, String lastName, String phone, String job, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.job = job;
        this.role = role;
    }
}
