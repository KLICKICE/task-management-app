package mate.academy.taskmanagementapp.service.task;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.TaskMapper;
import mate.academy.taskmanagementapp.model.Task;
import mate.academy.taskmanagementapp.model.TaskPriority;
import mate.academy.taskmanagementapp.model.TaskStatus;
import mate.academy.taskmanagementapp.model.User;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;

    @Override
    public TaskDto createTask(CreateTaskRequestDto dto) {
        Task task = taskMapper.toEntity(dto);
        User user = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        task.setAssignedUser(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        taskRepository.delete(task);
    }

    @Override
    public List<TaskDto> findAllByAssignedUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return taskRepository.findAllByAssignedUser(user).stream()
                .map(taskMapper::toDto).toList();
    }

    @Override
    public List<TaskDto> findAllByStatus(TaskStatus.StatusTask status) {
        return taskRepository.findAllByStatus(status).stream()
                .map(taskMapper::toDto).toList();
    }

    @Override
    public List<TaskDto> findAllByPriority(TaskPriority.PriorityStatus priority) {
        return taskRepository.findAllByPriority(priority).stream()
                .map(taskMapper::toDto).toList();
    }

    @Override
    public List<TaskDto> findAllByDeadlineBefore(LocalDateTime date) {
        return taskRepository.findAllByDeadlineBefore(date).stream()
                .map(taskMapper::toDto).toList();
    }

    @Override
    public TaskDto updateTask(Long id, TaskUpdatedDto taskUpdatedDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        task.setUpdatedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        return taskMapper.toDto(updatedTask);
    }
}
