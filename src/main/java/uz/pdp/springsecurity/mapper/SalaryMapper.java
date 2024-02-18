package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.pdp.springsecurity.entity.Salary;
import uz.pdp.springsecurity.payload.SalaryGetDto;

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
