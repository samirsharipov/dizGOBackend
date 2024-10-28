package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Bonus;
import uz.pdp.springsecurity.payload.BonusGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-28T11:55:46+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class BonusMapperImpl implements BonusMapper {

    @Override
    public BonusGetDto toDto(Bonus bonus) {
        if ( bonus == null ) {
            return null;
        }

        BonusGetDto bonusGetDto = new BonusGetDto();

        bonusGetDto.setId( bonus.getId() );
        bonusGetDto.setName( bonus.getName() );
        bonusGetDto.setColor( bonus.getColor() );
        bonusGetDto.setIcon( bonus.getIcon() );
        bonusGetDto.setSumma( bonus.getSumma() );
        bonusGetDto.setActive( bonus.isActive() );

        return bonusGetDto;
    }

    @Override
    public List<BonusGetDto> toDtoList(List<Bonus> bonusList) {
        if ( bonusList == null ) {
            return null;
        }

        List<BonusGetDto> list = new ArrayList<BonusGetDto>( bonusList.size() );
        for ( Bonus bonus : bonusList ) {
            list.add( toDto( bonus ) );
        }

        return list;
    }
}
