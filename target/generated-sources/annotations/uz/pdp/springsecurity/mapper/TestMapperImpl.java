package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uz.pdp.springsecurity.entity.Test;
import uz.pdp.springsecurity.payload.TestDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-01T16:39:34+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.13 (Amazon.com Inc.)"
)
@Component
public class TestMapperImpl implements TestMapper {

    @Override
    public TestDto toDto(Test test) {
        if ( test == null ) {
            return null;
        }

        TestDto testDto = new TestDto();

        testDto.setId( test.getId() );
        testDto.setQuestion( test.getQuestion() );
        testDto.setA1( test.getA1() );
        testDto.setA2( test.getA2() );
        testDto.setA3( test.getA3() );
        testDto.setA4( test.getA4() );

        return testDto;
    }

    @Override
    public List<TestDto> toDtoList(List<Test> testList) {
        if ( testList == null ) {
            return null;
        }

        List<TestDto> list = new ArrayList<TestDto>( testList.size() );
        for ( Test test : testList ) {
            list.add( toDto( test ) );
        }

        return list;
    }
}
