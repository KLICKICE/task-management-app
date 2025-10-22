package mate.academy.taskmanagementapp.service.task;

import java.time.LocalDateTime;
import java.util.List;

import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.model.TaskPriority;
import mate.academy.taskmanagementapp.model.TaskStatus;

public interface TaskService {
    TaskDto createTask(CreateTaskRequestDto createTaskRequestDto);

    void deleteTask(Long id);

    List<TaskDto> findAllByAssignedUser(Long userId);

    List<TaskDto> findAllByStatus(TaskStatus.StatusTask status);

    List<TaskDto> findAllByPriority(TaskPriority.PriorityStatus priority);

    List<TaskDto> findAllByDeadlineBefore(LocalDateTime date);

    TaskDto updateTask(Long id, TaskUpdatedDto taskUpdatedDto);
}

