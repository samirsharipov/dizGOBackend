package uz.pdp.springsecurity.shoxjaxon.activity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class Customer2 {
    private UUID id;
    private LocalDateTime updateAt;
    private boolean active;
    private LocalDate birthday;
    private double debt;
    private String description;
    private String lidCustomer;
    private String name;
    private LocalDate payDate;
    private String phoneNumber;
    private String telegram;
    private UUID branchId;
    private UUID businessId;
    private UUID customerGroupId;
    private String branchName; // New field for branch name





    // Continue with other fields...

    // Getters and Setters

    // Constructors (Default and parameterized)
}