package mate.academy.taskmanagementapp.service.task;

import java.util.List;

import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;

public interface TaskService {
    TaskDto createTask(CreateTaskRequestDto createTaskRequestDto);

    TaskDto getTaskById(Long id);

    TaskDto updateTask(Long id, TaskUpdatedDto taskUpdatedDto);

    List<TaskDto> getTasksByProjectId(Long projectId);

    void deleteTask(Long id);
}


