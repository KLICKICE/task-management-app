package mate.academy.taskmanagementapp.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.task.TaskPriority;
import mate.academy.taskmanagementapp.model.task.TaskStatus;
import mate.academy.taskmanagementapp.model.user.User;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TaskRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProjectStatusRepository projectStatusRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    private User savedUser;
    private Project savedProject;
    private TaskStatus savedStatus;
    private TaskPriority savedPriority;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        Role savedRole = roleRepository.save(role);

        User user = new User();
        user.setFirstName("Sunless");
        user.setLastName("Leywin");
        user.setUsername("LostFromLight");
        user.setEmail("sunless@gmail.com");
        user.setPassword(passwordEncoder.encode("Sunless07062006"));
        user.setRoles(Set.of(savedRole));
        savedUser = userRepository.save(user);

        ProjectStatus projectStatus = new ProjectStatus();
        projectStatus.setStatusProject(ProjectStatus.StatusProject.INITIATED);
        ProjectStatus savedProjectStatus = projectStatusRepository.save(projectStatus);

        Project project = new Project();
        project.setTitle("Shadowfall System");
        project.setDescription("Born from darkness and persistence.");
        project.setOwner(savedUser);
        project.setStatus(savedProjectStatus);
        project.setStartDate(LocalDate.now());
        savedProject = projectRepository.save(project);

        TaskStatus status = new TaskStatus();
        status.setStatusTask(TaskStatus.StatusTask.NEW);
        savedStatus = taskStatusRepository.save(status);

        TaskPriority priority = new TaskPriority();
        priority.setPriorityStatus(TaskPriority.PriorityStatus.HIGH);
        savedPriority = taskPriorityRepository.save(priority);
    }

    @Test
    @DisplayName("Save a task successfully")
    void saveTask_Success() {
        Task task = new Task();
        task.setTitle("Implement authentication");
        task.setDescription("Develop secure login and registration flow.");
        task.setProject(savedProject);
        task.setStatus(savedStatus);
        task.setPriority(savedPriority);
        task.setAssignedUser(savedUser);
        task.setDeadline(LocalDateTime.now().plusDays(5));

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());
        assertEquals("Implement authentication", savedTask.getTitle());
        assertEquals(TaskStatus.StatusTask.NEW, savedTask.getStatus().getStatusTask());
        assertEquals(TaskPriority.PriorityStatus.HIGH, savedTask.getPriority().getPriorityStatus());
        assertEquals(savedUser.getId(), savedTask.getAssignedUser().getId());
        assertEquals(savedProject.getId(), savedTask.getProject().getId());
    }

    @Test
    @DisplayName("Find task by Status, success")
    void findAllByStatus_StatusTask() {
        Task task = new Task();
        task.setTitle("Implement authentication");
        task.setDescription("Develop secure login and registration flow.");
        task.setProject(savedProject);
        task.setStatus(savedStatus);
        task.setPriority(savedPriority);
        task.setAssignedUser(savedUser);
        task.setDeadline(LocalDateTime.now().plusDays(5));

        Task savedTask = taskRepository.save(task);

        List<Task> tasks = taskRepository
                .findAllByStatus_StatusTask(savedTask.getStatus().getStatusTask());

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty(), "Expected tasks with status NEW");
        assertEquals(TaskStatus.StatusTask.NEW, tasks.get(0).getStatus().getStatusTask());
    }
}
