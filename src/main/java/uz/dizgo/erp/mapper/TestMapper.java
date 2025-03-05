package uz.dizgo.erp.mapper;

import org.mapstruct.Mapper;
import uz.dizgo.erp.entity.Test;
import uz.dizgo.erp.payload.TestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestMapper {
    TestDto toDto(Test test);

    List<TestDto> toDtoList(List<Test> testList);
}
