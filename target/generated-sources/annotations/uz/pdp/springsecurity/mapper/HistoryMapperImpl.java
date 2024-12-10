package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.History;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.HistoryGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T12:12:11+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class HistoryMapperImpl implements HistoryMapper {

    @Override
    public HistoryGetDto toDto(History history) {
        if ( history == null ) {
            return null;
        }

        HistoryGetDto historyGetDto = new HistoryGetDto();

        historyGetDto.setFirstName( historyUserFirstName( history ) );
        historyGetDto.setLastName( historyUserLastName( history ) );
        historyGetDto.setBranchName( historyBranchName( history ) );
        historyGetDto.setId( history.getId() );
        historyGetDto.setCreatedAt( history.getCreatedAt() );
        historyGetDto.setName( history.getName() );
        historyGetDto.setDescription( history.getDescription() );

        return historyGetDto;
    }

    @Override
    public List<HistoryGetDto> toDtoList(List<History> historyList) {
        if ( historyList == null ) {
            return null;
        }

        List<HistoryGetDto> list = new ArrayList<HistoryGetDto>( historyList.size() );
        for ( History history : historyList ) {
            list.add( toDto( history ) );
        }

        return list;
    }

    private String historyUserFirstName(History history) {
        if ( history == null ) {
            return null;
        }
        User user = history.getUser();
        if ( user == null ) {
            return null;
        }
        String firstName = user.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String historyUserLastName(History history) {
        if ( history == null ) {
            return null;
        }
        User user = history.getUser();
        if ( user == null ) {
            return null;
        }
        String lastName = user.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }

    private String historyBranchName(History history) {
        if ( history == null ) {
            return null;
        }
        Branch branch = history.getBranch();
        if ( branch == null ) {
            return null;
        }
        String name = branch.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
