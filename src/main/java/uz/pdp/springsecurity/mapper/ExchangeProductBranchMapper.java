package uz.pdp.springsecurity.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.pdp.springsecurity.entity.ExchangeProductBranch;
import uz.pdp.springsecurity.payload.ExchangeProductBranchDTO;

import java.util.List;


@Mapper(componentModel = "spring")
public interface ExchangeProductBranchMapper {

    @Mapping(target = "delete", ignore = true)
    @Mapping(target = "exchangeProductList", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "shippedBranch", ignore = true)
    @Mapping(target = "receivedBranch", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exchangeStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "business", ignore = true)
    @Mapping(target = "shippedBranch.id", source = "shippedBranchId")
    @Mapping(target = "receivedBranch.id", source = "receivedBranchId")
    @Mapping(target = "exchangeStatus.id", source = "exchangeStatusId")
    @Mapping(target = "business.id", source = "businessId")
    ExchangeProductBranch toEntity(ExchangeProductBranchDTO exchangeProductBranchDTO);


    @Mapping(target = "exchangeProductDTOS", ignore = true)
    @Mapping(target = "shippedBranchId", source = "shippedBranch.id")
    @Mapping(target = "receivedBranchId", source = "receivedBranch.id")
    @Mapping(target = "exchangeStatusId", source = "exchangeStatus.id")
    @Mapping(target = "businessId", source = "business.id")
    ExchangeProductBranchDTO toDto(ExchangeProductBranch branch);

    List<ExchangeProductBranchDTO> toDtoList(List<ExchangeProductBranch> branchList);

    @InheritInverseConfiguration
    @Mapping(target = "exchangeProductList", ignore = true)
    @Mapping(target = "shippedBranch.id", source = "shippedBranchId")
    @Mapping(target = "receivedBranch.id", source = "receivedBranchId")
    @Mapping(target = "exchangeStatus.id", source = "exchangeStatusId")
    @Mapping(target = "business.id", source = "businessId")
    void update(ExchangeProductBranchDTO exchangeProductBranchDTO, @MappingTarget ExchangeProductBranch exchangeProductBranch);
}
