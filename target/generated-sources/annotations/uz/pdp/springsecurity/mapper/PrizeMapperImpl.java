package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Bonus;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Prize;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.PrizeGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-03T14:32:05+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class PrizeMapperImpl implements PrizeMapper {

    @Override
    public PrizeGetDto toDto(Prize prize) {
        if ( prize == null ) {
            return null;
        }

        PrizeGetDto prizeGetDto = new PrizeGetDto();

        prizeGetDto.setBranchId( prizeBranchId( prize ) );
        prizeGetDto.setBranchName( prizeBranchName( prize ) );
        prizeGetDto.setBonusId( prizeBonusId( prize ) );
        prizeGetDto.setBonusName( prizeBonusName( prize ) );
        prizeGetDto.setBonusColor( prizeBonusColor( prize ) );
        prizeGetDto.setBonusIcon( prizeBonusIcon( prize ) );
        prizeGetDto.setBonusSumma( prizeBonusSumma( prize ) );
        prizeGetDto.setUserId( prizeUserId( prize ) );
        prizeGetDto.setFirstName( prizeUserFirstName( prize ) );
        prizeGetDto.setLastName( prizeUserLastName( prize ) );
        prizeGetDto.setId( prize.getId() );
        prizeGetDto.setDate( prize.getDate() );
        prizeGetDto.setDescription( prize.getDescription() );
        prizeGetDto.setGiven( prize.isGiven() );
        prizeGetDto.setTask( prize.isTask() );
        prizeGetDto.setLid( prize.isLid() );
        prizeGetDto.setCount( prize.getCount() );
        prizeGetDto.setDeadline( prize.getDeadline() );
        prizeGetDto.setCounter( prize.getCounter() );

        return prizeGetDto;
    }

    @Override
    public List<PrizeGetDto> toDtoList(List<Prize> prizeList) {
        if ( prizeList == null ) {
            return null;
        }

        List<PrizeGetDto> list = new ArrayList<PrizeGetDto>( prizeList.size() );
        for ( Prize prize : prizeList ) {
            list.add( toDto( prize ) );
        }

        return list;
    }

    private UUID prizeBranchId(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        Branch branch = prize.getBranch();
        if ( branch == null ) {
            return null;
        }
        UUID id = branch.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String prizeBranchName(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        Branch branch = prize.getBranch();
        if ( branch == null ) {
            return null;
        }
        String name = branch.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID prizeBonusId(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        Bonus bonus = prize.getBonus();
        if ( bonus == null ) {
            return null;
        }
        UUID id = bonus.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String prizeBonusName(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        Bonus bonus = prize.getBonus();
        if ( bonus == null ) {
            return null;
        }
        String name = bonus.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String prizeBonusColor(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        Bonus bonus = prize.getBonus();
        if ( bonus == null ) {
            return null;
        }
        String color = bonus.getColor();
        if ( color == null ) {
            return null;
        }
        return color;
    }

    private String prizeBonusIcon(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        Bonus bonus = prize.getBonus();
        if ( bonus == null ) {
            return null;
        }
        String icon = bonus.getIcon();
        if ( icon == null ) {
            return null;
        }
        return icon;
    }

    private double prizeBonusSumma(Prize prize) {
        if ( prize == null ) {
            return 0.0d;
        }
        Bonus bonus = prize.getBonus();
        if ( bonus == null ) {
            return 0.0d;
        }
        double summa = bonus.getSumma();
        return summa;
    }

    private UUID prizeUserId(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        User user = prize.getUser();
        if ( user == null ) {
            return null;
        }
        UUID id = user.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String prizeUserFirstName(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        User user = prize.getUser();
        if ( user == null ) {
            return null;
        }
        String firstName = user.getFirstName();
        if ( firstName == null ) {
            return null;
        }
        return firstName;
    }

    private String prizeUserLastName(Prize prize) {
        if ( prize == null ) {
            return null;
        }
        User user = prize.getUser();
        if ( user == null ) {
            return null;
        }
        String lastName = user.getLastName();
        if ( lastName == null ) {
            return null;
        }
        return lastName;
    }
}
