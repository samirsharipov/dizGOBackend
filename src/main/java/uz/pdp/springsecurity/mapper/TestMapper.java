package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import uz.pdp.springsecurity.entity.Test;
import uz.pdp.springsecurity.payload.TestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestDto toDto(Test test);

    List<TestDto> toDtoList(List<Test> testList);
}
