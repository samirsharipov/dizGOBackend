package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Salary;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.SalaryGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-03T14:32:05+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class SalaryMapperImpl implements SalaryMapper {

    @Override
    public SalaryGetDto toDto(Salary salary) {
        if ( salary == null ) {
            return null;
        }

        SalaryGetDto salaryGetDto = new SalaryGetDto();

        salaryGetDto.setLastName( salaryUserLastName( salary ) );
        salaryGetDto.setFirstName( salaryUserFirstName( salary ) );
        salaryGetDto.setUserId( salaryUserId( salary ) );
        salaryGetDto.setSalaryId( salary.getId() );
        salaryGetDto.setRemain( salary.getRemain() );
        salaryGetDto.setSalary( salary.getSalary() );
        salaryGetDto.setPayedSum( salary.getPayedSum() );
        salaryGetDto.setStartDate( salary.getStartDate() );
        salaryGetDto.setEndDate( salary.getEndDate() );
        salaryGetDto.setActive( salary.isActive() );
        salaryGetDto.setDescription( salary.getDescription() );
        salaryGetDto.setShouldPaySum( salary.getShouldPaySum() );

        return salaryGetDto;
    }

    @Override
    public List<SalaryGetDto> toDtoList(List<Salary> salaryList) {
        if ( salaryList == null ) {
            return null;
        }

        List<SalaryGetDto> list = new ArrayList<SalaryGetDto>( salaryList.size() );
        for ( Salary salary : salaryList ) {
            list.add( toDto( salary ) );
        }

        return list;
    }

    private String salaryUserLastName(Salary salary) {
        if ( salary == null ) {
            return null;
        }
        User user = salary.getUser();
        if ( user == null ) {
            return null;
        }
        String lastName = user.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String salaryUserFirstName(Salary salary) {
        if ( salary == null ) {
            return null;
        }
        User user = salary.getUser();
        if ( user == null ) {
            return null;
        }
        String firstName = user.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private UUID salaryUserId(Salary salary) {
        if ( salary == null ) {
            return null;
        }
        User user = salary.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
