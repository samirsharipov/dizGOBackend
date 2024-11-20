package uz.pdp.springsecurity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.processing.Generated;
import uz.pdp.springsecurity.entity.Branch;
import uz.pdp.springsecurity.entity.Content;
import uz.pdp.springsecurity.entity.Production;
import uz.pdp.springsecurity.entity.Project;
import uz.pdp.springsecurity.entity.Stage;
import uz.pdp.springsecurity.entity.Task;
import uz.pdp.springsecurity.entity.TaskPrice;
import uz.pdp.springsecurity.entity.TaskStatus;
import uz.pdp.springsecurity.entity.TaskType;
import uz.pdp.springsecurity.payload.TaskGetDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-20T14:57:48+0500",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
public class TaskMapperImpl implements TaskMapper {

    @Override
    public TaskGetDto toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskGetDto taskGetDto = new TaskGetDto();

        taskGetDto.setStageId( taskStageId( task ) );
        taskGetDto.setProjectId( taskProjectId( task ) );
        taskGetDto.setContentId( taskContentId( task ) );
        taskGetDto.setProductionId( taskProductionId( task ) );
        taskGetDto.setProjectName( taskProjectName( task ) );
        taskGetDto.setTaskTypeId( taskTaskTypeId( task ) );
        List<TaskPrice> list = task.getTaskPriceList();
        if ( list != null ) {
            taskGetDto.setTaskPriceList( new ArrayList<TaskPrice>( list ) );
        }
        taskGetDto.setTaskStatusId( taskTaskStatusId( task ) );
        taskGetDto.setBranchId( taskBranchId( task ) );
        taskGetDto.setDependTask( taskDependTaskId( task ) );
        taskGetDto.setId( task.getId() );
        taskGetDto.setName( task.getName() );
        taskGetDto.setDeadLine( task.getDeadLine() );
        taskGetDto.setStartDate( task.getStartDate() );
        taskGetDto.setEndDate( task.getEndDate() );
        if ( task.getImportance() != null ) {
            taskGetDto.setImportance( task.getImportance().name() );
        }
        taskGetDto.setProductions( task.isProductions() );
        taskGetDto.setGoalAmount( task.getGoalAmount() );
        taskGetDto.setTaskPrice( task.getTaskPrice() );
        taskGetDto.setDescription( task.getDescription() );
        taskGetDto.setProduction( task.getProduction() );

        return taskGetDto;
    }

    @Override
    public List<TaskGetDto> toDto(List<Task> taskList) {
        if ( taskList == null ) {
            return null;
        }

        List<TaskGetDto> list = new ArrayList<TaskGetDto>( taskList.size() );
        for ( Task task : taskList ) {
            list.add( toDto( task ) );
        }

        return list;
    }

    private UUID taskStageId(Task task) {
        if ( task == null ) {
            return null;
        }
        Stage stage = task.getStage();
        if ( stage == null ) {
            return null;
        }
        UUID id = stage.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID taskProjectId(Task task) {
        if ( task == null ) {
            return null;
        }
        Project project = task.getProject();
        if ( project == null ) {
            return null;
        }
        UUID id = project.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID taskContentId(Task task) {
        if ( task == null ) {
            return null;
        }
        Content content = task.getContent();
        if ( content == null ) {
            return null;
        }
        UUID id = content.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID taskProductionId(Task task) {
        if ( task == null ) {
            return null;
        }
        Production production = task.getProduction();
        if ( production == null ) {
            return null;
        }
        UUID id = production.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String taskProjectName(Task task) {
        if ( task == null ) {
            return null;
        }
        Project project = task.getProject();
        if ( project == null ) {
            return null;
        }
        String name = project.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private UUID taskTaskTypeId(Task task) {
        if ( task == null ) {
            return null;
        }
        TaskType taskType = task.getTaskType();
        if ( taskType == null ) {
            return null;
        }
        UUID id = taskType.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID taskTaskStatusId(Task task) {
        if ( task == null ) {
            return null;
        }
        TaskStatus taskStatus = task.getTaskStatus();
        if ( taskStatus == null ) {
            return null;
        }
        UUID id = taskStatus.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID taskBranchId(Task task) {
        if ( task == null ) {
            return null;
        }
        Branch branch = task.getBranch();
        if ( branch == null ) {
            return null;
        }
        UUID id = branch.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private UUID taskDependTaskId(Task task) {
        if ( task == null ) {
            return null;
        }
        Task dependTask = task.getDependTask();
        if ( dependTask == null ) {
            return null;
        }
        UUID id = dependTask.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
