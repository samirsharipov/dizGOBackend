package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import uz.pdp.springsecurity.entity.Bonus;
import uz.pdp.springsecurity.payload.BonusGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BonusMapper {

    BonusGetDto toDto(Bonus bonus);

    List<BonusGetDto> toDtoList(List<Bonus> bonusList);
}
