package uz.pdp.springsecurity.payload;

import lombok.Data;

@Data
public class CustomerRegisterDto {
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
}
