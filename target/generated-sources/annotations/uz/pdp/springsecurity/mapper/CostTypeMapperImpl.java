package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.CostType;
import uz.pdp.springsecurity.payload.CostTypeDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-02T16:18:49+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class CostTypeMapperImpl implements CostTypeMapper {

    @Override
    public CostTypeDto toDto(CostType costType) {
        if ( costType == null ) {
            return null;
        }

        CostTypeDto costTypeDto = new CostTypeDto();

        costTypeDto.setBranchId( costTypeBranchId( costType ) );
        costTypeDto.setId( costType.getId() );
        costTypeDto.setName( costType.getName() );

        return costTypeDto;
    }

    @Override
    public List<CostTypeDto> toDtoList(List<CostType> costTypeList) {
        if ( costTypeList == null ) {
            return null;
        }

        List<CostTypeDto> list = new ArrayList<CostTypeDto>( costTypeList.size() );
        for ( CostType costType : costTypeList ) {
            list.add( toDto( costType ) );
        }

        return list;
    }

    private UUID costTypeBranchId(CostType costType) {
        if ( costType == null ) {
            return null;
        }
        Branch branch = costType.getBranch();
        if ( branch == null ) {
            return null;
        }
        UUID id = branch.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
