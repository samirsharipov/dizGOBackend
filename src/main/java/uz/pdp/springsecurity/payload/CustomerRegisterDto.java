package uz.pdp.springsecurity.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerRegisterDto {
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private UUID userId;
}
