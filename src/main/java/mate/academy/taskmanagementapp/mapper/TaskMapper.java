package mate.academy.taskmanagementapp.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.model.label.Label;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.task.TaskPriority;
import mate.academy.taskmanagementapp.model.task.TaskStatus;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapConfig.class)
public interface TaskMapper {

    @BeanMapping(ignoreUnmappedSourceProperties = {"projectId"})
    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Task toEntity(CreateTaskRequestDto dto);

    @Mapping(target = "assignedUserEmail", source = "assignedUser.email")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "taskPriority", source = "priority", qualifiedByName = "priorityToTitleCase")
    @Mapping(target = "labelIds", source = "labels", qualifiedByName = "labelsToIds")
    TaskDto toDto(Task task);

    @Mapping(target = "assignedUser", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "deadline", source = "deadline")
    void updateTaskFromDto(TaskUpdatedDto dto, @MappingTarget Task task);

    @Named("statusToString")
    default String statusToString(TaskStatus status) {
        return status == null ? null : status.name();
    }

    @Named("priorityToTitleCase")
    default String priorityToTitleCase(TaskPriority priority) {
        if (priority == null) {
            return null;
        }
        String s = priority.name().toLowerCase();
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    @Named("labelsToIds")
    default Set<Long> labelsToIds(Set<Label> labels) {
        if (labels == null || labels.isEmpty()) {
            return Collections.emptySet();
        }
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }
}
