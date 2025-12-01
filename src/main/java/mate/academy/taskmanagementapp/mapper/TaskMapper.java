package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.model.task.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapConfig.class)
public interface TaskMapper {

    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(CreateTaskRequestDto dto);

    @Mapping(target = "assignedUserEmail", source = "assignedUser.email")
    @Mapping(target = "taskStatus", source = "status.statusTask")
    @Mapping(target = "taskPriority", source = "priority.priorityStatus")
    TaskDto toDto(Task task);

    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateTaskFromDto(TaskUpdatedDto dto, @MappingTarget Task task);
}
