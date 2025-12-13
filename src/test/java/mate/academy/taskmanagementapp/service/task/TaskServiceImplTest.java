package mate.academy.taskmanagementapp.service.task;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
import mate.academy.taskmanagementapp.mapper.TaskMapper;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AuthServiceHelper authServiceHelper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private User user;
    private Role role;
    private Task task;
    private CreateTaskRequestDto createTaskRequestDto;
    private TaskDto taskDto;
    private TaskUpdatedDto taskUpdatedDto;
    private Project project;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);

        user = new User();
        user.setId(1L);
        user.setUsername("LostFromLight");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(role));

        project = new Project(); // ✅ додали
        project.setId(1L);
        project.setOwner(user);

        task = new Task();
        task.setId(1L);
        task.setTitle("Implement authentication");
        task.setDescription("Develop secure login and registration flow.");
        task.setAssignedUser(user);
        task.setProject(project); // ✅ важливо для projectId

        createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setTitle("Implement authentication");
        createTaskRequestDto.setAssignedUserId(user.getId());

        taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Implement authentication");

        taskUpdatedDto = new TaskUpdatedDto();
        taskUpdatedDto.setTitle("Update authentication flow");
    }

    @Test
    @DisplayName("Create Task, success")
    void createTask_Success() {
        Long id = 1L;

        when(taskMapper.toEntity(createTaskRequestDto)).thenReturn(task);
        when(userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto actual = taskService.createTask(createTaskRequestDto);

        assertNotNull(actual);
        assertEquals("Implement authentication", actual.getTitle());

        verify(taskMapper).toEntity(createTaskRequestDto);
        verify(userRepository).findById(id);
        verify(taskRepository).save(task);
        verify(taskMapper).toDto(task);
    }

    @Test
    @DisplayName("Get all tasks by projectId, success")
    void getTasksByProjectId_success() {
        Long projectId = 1L;

        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        when(taskRepository.findAllByProjectId(projectId)).thenReturn(List.of(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        List<TaskDto> actual = taskService.getTasksByProjectId(projectId);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals("Implement authentication", actual.get(0).getTitle());

        verify(authServiceHelper).getCurrentUser();
        verify(projectRepository).findById(projectId);
        verify(taskRepository).findAllByProjectId(projectId);
        verify(taskMapper).toDto(task);
    }

    @Test
    @DisplayName("Get task by Id, success")
    void getTask_ById_success() {
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        TaskDto actual = taskService.getTaskById(id);

        assertNotNull(actual);
        verify(taskRepository).findById(id);
        verify(taskMapper).toDto(task);
    }

    @Test
    @DisplayName("Delete task by Id, success")
    void deleteTask_ById_success() {
        Long id = 1L;

        when(taskRepository.findById(id)).thenReturn(Optional.ofNullable(task));
        when(authServiceHelper.getCurrentUser()).thenReturn(user);

        assertDoesNotThrow(() -> taskService.deleteTask(id));

        verify(taskRepository).findById(id);
        verify(authServiceHelper).getCurrentUser();
        verify(taskRepository).delete(task);
    }
}
