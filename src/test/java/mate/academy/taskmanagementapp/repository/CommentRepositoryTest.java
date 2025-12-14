package mate.academy.taskmanagementapp.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import mate.academy.taskmanagementapp.model.comment.Comment;
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
class CommentRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private CommentRepository commentRepository;

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
    private Task savedTask;

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
        TaskStatus savedStatus = taskStatusRepository.save(status);

        TaskPriority priority = new TaskPriority();
        priority.setPriorityStatus(TaskPriority.PriorityStatus.HIGH);
        TaskPriority savedPriority = taskPriorityRepository.save(priority);

        Task task = new Task();
        task.setTitle("Implement authentication");
        task.setDescription("Develop secure login and registration flow.");
        task.setProject(savedProject);
        task.setStatus(savedStatus);
        task.setPriority(savedPriority);
        task.setAssignedUser(savedUser);
        task.setDeadline(LocalDateTime.now().plusDays(5));
        savedTask = taskRepository.save(task);
    }

    @Test
    @DisplayName("Save a comment successfully")
    void saveComment_Success() {
        Comment comment = new Comment();
        comment.setText("Authentication logic implemented successfully!");
        comment.setTask(savedTask);
        comment.setUser(savedUser);

        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment.getId());
        assertEquals("Authentication logic implemented successfully!", savedComment.getText());
        assertEquals(savedUser.getId(), savedComment.getUser().getId());
        assertEquals(savedTask.getId(), savedComment.getTask().getId());
    }

    @Test
    @DisplayName("Find comments by task successfully")
    void findComments_By_Task_Success() {
        Comment comment = new Comment();
        comment.setText("Initial commit for login module.");
        comment.setTask(savedTask);
        comment.setUser(savedUser);
        commentRepository.save(comment);

        List<Comment> comments = commentRepository.findAllByTask(savedTask);

        assertNotNull(comments);
        assertFalse(comments.isEmpty());
        assertEquals("Initial commit for login module.", comments.get(0).getText());
    }
}
