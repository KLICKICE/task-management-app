package mate.academy.taskmanagementapp.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.model.task.TaskPriority;
import mate.academy.taskmanagementapp.model.task.TaskStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.task.Task;
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

    private User savedUser;
    private Project savedProject;

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

        Project project = new Project();
        project.setTitle("Shadowfall System");
        project.setDescription("Born from darkness and persistence.");
        project.setOwner(savedUser);
        project.setStatus(ProjectStatus.INITIATED); // ✅ ENUM
        project.setStartDate(LocalDate.now());

        savedProject = projectRepository.save(project);
    }

    @Test
    @DisplayName("Save a task successfully")
    void saveTask_Success() {
        Task task = new Task();
        task.setTitle("Implement authentication");
        task.setDescription("Develop secure login and registration flow.");
        task.setProject(savedProject);
        task.setStatus(TaskStatus.NEW);
        task.setPriority(TaskPriority.HIGH);
        task.setAssignedUser(savedUser);
        task.setDeadline(LocalDateTime.now().plusDays(5));

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask.getId());
        assertEquals("Implement authentication", savedTask.getTitle());
        assertEquals(TaskStatus.NEW, savedTask.getStatus());
        assertEquals(TaskPriority.HIGH, savedTask.getPriority());
        assertEquals(savedUser.getId(), savedTask.getAssignedUser().getId());
        assertEquals(savedProject.getId(), savedTask.getProject().getId());
    }

    @Test
    @DisplayName("Find task by status successfully")
    void findAllByStatus_success() {
        Task task = new Task();
        task.setTitle("Implement authentication");
        task.setDescription("Develop secure login and registration flow.");
        task.setProject(savedProject);
        task.setStatus(TaskStatus.NEW);
        task.setPriority(TaskPriority.HIGH);
        task.setAssignedUser(savedUser);
        task.setDeadline(LocalDateTime.now().plusDays(5));

        taskRepository.save(task);

        List<Task> tasks = taskRepository.findAllByStatus(TaskStatus.NEW);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(TaskStatus.NEW, tasks.get(0).getStatus());
    }
}


