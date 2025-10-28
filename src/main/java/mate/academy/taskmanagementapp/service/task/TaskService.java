package mate.academy.taskmanagementapp.service.task;

import java.util.List;

import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;

public interface TaskService {
    TaskDto createTask(CreateTaskRequestDto createTaskRequestDto);

    List<TaskDto> getAllTasks();

    TaskDto getTaskById(Long id);

    TaskDto updateTask(Long id, TaskUpdatedDto taskUpdatedDto);

    void deleteTask(Long id);
}


