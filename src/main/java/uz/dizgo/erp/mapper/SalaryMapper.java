package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.dizgo.erp.entity.Salary;
import uz.dizgo.erp.payload.SalaryGetDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalaryMapper {
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "salaryId", source = "id")
    SalaryGetDto toDto(Salary salary);

    List<SalaryGetDto> toDtoList(List<Salary> salaryList);
}
