package uz.pdp.springsecurity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uz.pdp.springsecurity.entity.Attachment;
import uz.pdp.springsecurity.entity.Task;
import uz.pdp.springsecurity.entity.User;
import uz.pdp.springsecurity.payload.TaskGetDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Mapper()
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(source = "stage.id", target = "stageId")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(target = "fileDataIdList", ignore = true)
    @Mapping(target = "contentId", source = "content.id")
    @Mapping(target = "productionId", source = "production.id")
    @Mapping(target = "projectName", source = "project.name")
    @Mapping(target = "taskTypeId", source = "taskType.id")
    @Mapping(target = "taskPriceList", source = "taskPriceList")
    @Mapping(target = "taskStatusId", source = "taskStatus.id")
    @Mapping(target = "branchId", source = "branch.id")
    @Mapping(target = "dependTask", source = "dependTask.id")
    @Mapping(target = "contentProductList", ignore = true)
    @Mapping(target = "costGetDtoList", ignore = true)
    @Mapping(target = "id", source = "task.id")
    TaskGetDto toDto(Task task);

    default List<User> mapUsers(Set<User> users) {
        return new ArrayList<>(users);
    }

    List<TaskGetDto> toDto(List<Task> taskList);
}
