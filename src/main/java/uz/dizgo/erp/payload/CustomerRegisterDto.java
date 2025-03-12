package uz.dizgo.erp.payload;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CustomerRegisterDto {
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private UUID userId;
    private Date birthday;
    private boolean sex;// true erkak false ayol
}
