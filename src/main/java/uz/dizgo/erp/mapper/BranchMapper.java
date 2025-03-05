package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Branch;
import uz.dizgo.erp.payload.BranchDto;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "branchCategory", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business.id", source = "businessId")
    @Mapping(target = "address.id", source = "addressId")
    Branch toEntity(BranchDto branchDto);
}
