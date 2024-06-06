package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Agreement;
import uz.pdp.springsecurity.entity.SalaryCount;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.enums.SalaryStatus;
import uz.pdp.springsecurity.payload.SalaryCountGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-06T08:25:15+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
@Component
public class SalaryCountMapperImpl implements SalaryCountMapper {

    @Override
    public SalaryCountGetDto toGetDto(SalaryCount salaryCount) {
        if ( salaryCount == null ) {
            return null;
        }

        SalaryCountGetDto salaryCountGetDto = new SalaryCountGetDto();

        salaryCountGetDto.setUserId( salaryCountAgreementUserId( salaryCount ) );
        salaryCountGetDto.setFirstName( salaryCountAgreementUserFirstName( salaryCount ) );
        salaryCountGetDto.setLastName( salaryCountAgreementUserLastName( salaryCount ) );
        SalaryStatus salaryStatus = salaryCountAgreementSalaryStatus( salaryCount );
        if ( salaryStatus != null ) {
            salaryCountGetDto.setAgreementName( salaryStatus.name() );
        }
        salaryCountGetDto.setAgreementId( salaryCountAgreementId( salaryCount ) );
        salaryCountGetDto.setId( salaryCount.getId() );
        salaryCountGetDto.setCount( salaryCount.getCount() );
        salaryCountGetDto.setSalary( salaryCount.getSalary() );
        salaryCountGetDto.setDate( salaryCount.getDate() );
        salaryCountGetDto.setDescription( salaryCount.getDescription() );

        return salaryCountGetDto;
    }

    @Override
    public List<SalaryCountGetDto> toGetDtoList(List<SalaryCount> salaryCountList) {
        if ( salaryCountList == null ) {
            return null;
        }

        List<SalaryCountGetDto> list = new ArrayList<SalaryCountGetDto>( salaryCountList.size() );
        for ( SalaryCount salaryCount : salaryCountList ) {
            list.add( toGetDto( salaryCount ) );
        }

        return list;
    }

    private UUID salaryCountAgreementUserId(SalaryCount salaryCount) {
        if ( salaryCount == null ) {
            return null;
        }
        Agreement agreement = salaryCount.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        User user = agreement.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String salaryCountAgreementUserFirstName(SalaryCount salaryCount) {
        if ( salaryCount == null ) {
            return null;
        }
        Agreement agreement = salaryCount.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        User user = agreement.getUser();
        if ( user == null ) {
            return null;
        }
        String firstName = user.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String salaryCountAgreementUserLastName(SalaryCount salaryCount) {
        if ( salaryCount == null ) {
            return null;
        }
        Agreement agreement = salaryCount.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        User user = agreement.getUser();
        if ( user == null ) {
            return null;
        }
        String lastName = user.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private SalaryStatus salaryCountAgreementSalaryStatus(SalaryCount salaryCount) {
        if ( salaryCount == null ) {
            return null;
        }
        Agreement agreement = salaryCount.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        SalaryStatus salaryStatus = agreement.getSalaryStatus();
        if ( salaryStatus == null ) {
            return null;
        }
        return salaryStatus;
    }

    private UUID salaryCountAgreementId(SalaryCount salaryCount) {
        if ( salaryCount == null ) {
            return null;
        }
        Agreement agreement = salaryCount.getAgreement();
        if ( agreement == null ) {
            return null;
        }
        UUID id = agreement.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
