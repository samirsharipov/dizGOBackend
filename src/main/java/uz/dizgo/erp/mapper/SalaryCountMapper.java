package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.SalaryCount;
import uz.dizgo.erp.payload.SalaryCountGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalaryCountMapper {

    @Mapping(target = "userId", source = "agreement.user.id")
    @Mapping(target = "firstName", source = "agreement.user.firstName")
    @Mapping(target = "lastName", source = "agreement.user.lastName")
    @Mapping(target = "agreementName", source = "agreement.salaryStatus")
    @Mapping(target = "agreementId", source = "agreement.id")
    SalaryCountGetDto toGetDto(SalaryCount salaryCount);

    List<SalaryCountGetDto> toGetDtoList(List<SalaryCount> salaryCountList);


}
