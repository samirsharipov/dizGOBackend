
package uz.dizgo.erp.shoxjaxon.activity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

public class User2 {
    @Getter
    @Setter
    private UUID id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String first_name;

    @Getter
    @Setter
    private String last_name;

    @Getter
    @Setter
    private String birthday;

    @Getter
    @Setter
    private String phone_number;

    @Getter
    @Setter
    private UUID photo_id;

    // Additional fields for attachment
    @Getter
    @Setter
    private UUID attachment_id;

    @Getter
    @Setter
    private LocalDateTime attachment_created_at;

    @Getter
    @Setter
    private LocalDateTime attachment_updated_at;

    @Getter
    @Setter
    private String content_type;

    @Getter
    @Setter
    private String file_original_name;

    @Getter
    @Setter
    private String attachment_name;

    @Getter
    @Setter
    private String role_id;

    @Getter
    @Setter
    private String arrival_time;

    @Getter
    @Setter
    private String leave_time;

    @Getter
    @Setter
    private String branch_names;

    @Getter
    @Setter
    private String branch_name;


    private Long size;

    @Getter
    @Setter
    private UUID branch_id;

    // You may need to adjust this based on your actual database schema
    @Getter
    @Setter
    private String attachment_photo_id;

    @Getter
    @Setter

    private String roleName;




    // ... other fields, constructors, and getters/setters
}
