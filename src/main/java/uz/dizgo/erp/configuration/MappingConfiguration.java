package uz.dizgo.erp.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uz.dizgo.erp.mapper.TaskMapper;

@Configuration
public class MappingConfiguration {

    @Bean
    public TaskMapper taskMapper() {
        return Mappers.getMapper(TaskMapper.class);
    }
}