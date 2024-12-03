package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Cost;
import uz.pdp.springsecurity.entity.CostType;
import uz.pdp.springsecurity.payload.CostGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-03T14:32:05+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class CostMapperImpl implements CostMapper {

    @Override
    public CostGetDto toDto(Cost cost) {
        if ( cost == null ) {
            return null;
        }

        CostGetDto costGetDto = new CostGetDto();

        costGetDto.setCostTypeId( costCostTypeId( cost ) );
        costGetDto.setCostTypeName( costCostTypeName( cost ) );
        costGetDto.setSum( cost.getSum() );

        return costGetDto;
    }

    @Override
    public List<CostGetDto> toDtoList(List<Cost> costList) {
        if ( costList == null ) {
            return null;
        }

        List<CostGetDto> list = new ArrayList<CostGetDto>( costList.size() );
        for ( Cost cost : costList ) {
            list.add( toDto( cost ) );
        }

        return list;
    }

    private UUID costCostTypeId(Cost cost) {
        if ( cost == null ) {
            return null;
        }
        CostType costType = cost.getCostType();
        if ( costType == null ) {
            return null;
        }
        UUID id = costType.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String costCostTypeName(Cost cost) {
        if ( cost == null ) {
            return null;
        }
        CostType costType = cost.getCostType();
        if ( costType == null ) {
            return null;
        }
        String name = costType.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}
