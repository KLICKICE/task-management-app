package mate.academy.taskmanagementapp.service.task;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.exception.AccessDeniedException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.TaskMapper;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final AuthServiceHelper authServiceHelper;

    @Override
    public TaskDto createTask(CreateTaskRequestDto dto) {
        Task task = taskMapper.toEntity(dto);
        User user = userRepository.findById(dto.getAssignedUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        task.setAssignedUser(user);
        task.setCreatedAt(LocalDateTime.now());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @Override
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDto updateTask(Long id, TaskUpdatedDto dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(task, currentUser);

        taskMapper.updateTaskFromDto(dto, task);
        task.setUpdatedAt(LocalDateTime.now());
        return taskMapper.toDto(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(task, currentUser);
        taskRepository.delete(task);
    }

    private void validateUserPermission(Task task, User currentUser) {
        boolean isAdmin = authServiceHelper.isAdmin(currentUser);
        boolean isOwner = task.getAssignedUser().getId().equals(currentUser.getId());
        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You are not allowed to modify this task");
        }
    }
}
