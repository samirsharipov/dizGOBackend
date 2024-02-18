package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.payload.BranchDto;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    @Mapping(target = "address.id", source = "addressId")
    Branch toEntity(BranchDto branchDto);
}
