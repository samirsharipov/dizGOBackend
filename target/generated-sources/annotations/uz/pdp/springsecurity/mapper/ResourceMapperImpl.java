package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Resource;
import uz.pdp.springsecurity.payload.ResourceDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-05T15:36:13+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class ResourceMapperImpl implements ResourceMapper {

    @Override
    public Resource toResource(ResourceDto resourceDto) {
        if ( resourceDto == null ) {
            return null;
        }

        Resource resource = new Resource();

        resource.setBranch( resourceDtoToBranch( resourceDto ) );
        resource.setId( resourceDto.getId() );
        resource.setName( resourceDto.getName() );
        resource.setDescription( resourceDto.getDescription() );
        resource.setPercentage( resourceDto.getPercentage() );
        resource.setTotalSum( resourceDto.getTotalSum() );
        resource.setStartDate( resourceDto.getStartDate() );
        resource.setEndDate( resourceDto.getEndDate() );
        resource.setDailyAmount( resourceDto.getDailyAmount() );
        resource.setLastUpdateDate( resourceDto.getLastUpdateDate() );

        return resource;
    }

    @Override
    public ResourceDto toResourceDto(Resource resource) {
        if ( resource == null ) {
            return null;
        }

        ResourceDto resourceDto = new ResourceDto();

        resourceDto.setBranchId( resourceBranchId( resource ) );
        resourceDto.setId( resource.getId() );
        resourceDto.setName( resource.getName() );
        resourceDto.setDescription( resource.getDescription() );
        resourceDto.setPercentage( resource.getPercentage() );
        resourceDto.setTotalSum( resource.getTotalSum() );
        resourceDto.setStartDate( resource.getStartDate() );
        resourceDto.setEndDate( resource.getEndDate() );
        resourceDto.setDailyAmount( resource.getDailyAmount() );
        resourceDto.setLastUpdateDate( resource.getLastUpdateDate() );

        return resourceDto;
    }

    @Override
    public List<ResourceDto> toResourceDtoList(List<Resource> resources) {
        if ( resources == null ) {
            return null;
        }

        List<ResourceDto> list = new ArrayList<ResourceDto>( resources.size() );
        for ( Resource resource : resources ) {
            list.add( toResourceDto( resource ) );
        }

        return list;
    }

    protected Branch resourceDtoToBranch(ResourceDto resourceDto) {
        if ( resourceDto == null ) {
            return null;
        }

        Branch branch = new Branch();

        branch.setId( resourceDto.getBranchId() );

        return branch;
    }

    private UUID resourceBranchId(Resource resource) {
        if ( resource == null ) {
            return null;
        }
        Branch branch = resource.getBranch();
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
