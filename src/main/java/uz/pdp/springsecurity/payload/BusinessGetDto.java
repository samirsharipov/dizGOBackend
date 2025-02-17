package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessGetDto {
    private Timestamp createdAt;
    private Timestamp updateAt;
    private UUID id;
    private String name;
    private String description;
    private boolean active;
    private boolean deleted;

    private Timestamp contractStartDate;
    private Timestamp contractEndDate;

    private String businessCategoryName;

}
