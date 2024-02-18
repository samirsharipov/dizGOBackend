package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestDto {
    private UUID id;

    private String question;

    private String a1;

    private String a2;

    private String a3;

    private String a4;
}
