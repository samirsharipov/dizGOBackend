package uz.pdp.springsecurity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgreementGetDto {
    @NotNull
    private Date startDate;

    @NotNull
    private Date endDate;

    @NotNull
    private List<AgreementDto> agreementDtoList;
}
