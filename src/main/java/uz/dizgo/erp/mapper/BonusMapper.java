package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import uz.dizgo.erp.entity.Bonus;
import uz.dizgo.erp.payload.BonusGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BonusMapper {

    BonusGetDto toDto(Bonus bonus);

    List<BonusGetDto> toDtoList(List<Bonus> bonusList);
}
