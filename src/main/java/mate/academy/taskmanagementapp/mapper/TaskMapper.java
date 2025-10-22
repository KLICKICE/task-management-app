package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapConfig.class)
public interface TaskMapper {
    Task toEntity(CreateTaskRequestDto createTaskRequestDto);

    TaskDto toDto(Task task);

    void updateTaskFromDto(TaskUpdatedDto dto, @MappingTarget Task task);
}
