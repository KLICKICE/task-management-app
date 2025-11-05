package mate.academy.taskmanagementapp.repository;

import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.jdbc.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.context.*;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.*;
import mate.academy.taskmanagementapp.model.attachment.Attachment;
import mate.academy.taskmanagementapp.model.project.*;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.task.*;
import mate.academy.taskmanagementapp.model.user.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AttachmentRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private AttachmentRepository attachmentRepository;

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
    @DisplayName("Save an attachment successfully")
    void saveAttachment_success() {
        Attachment attachment = new Attachment();
        attachment.setTask(savedTask);
        attachment.setDropboxFileId("dropbox-12345");
        attachment.setFileName("auth-module.zip");

        Attachment savedAttachment = attachmentRepository.save(attachment);

        assertNotNull(savedAttachment.getId());
        assertEquals("auth-module.zip", savedAttachment.getFileName());
        assertEquals("dropbox-12345", savedAttachment.getDropboxFileId());
        assertEquals(savedTask.getId(), savedAttachment.getTask().getId());
        assertNotNull(savedAttachment.getUploadDate());
    }

    @Test
    @DisplayName("Find all attachments by task id successfully")
    void findAllByTaskId_success() {
        Attachment attachment = new Attachment();
        attachment.setTask(savedTask);
        attachment.setDropboxFileId("dropbox-67890");
        attachment.setFileName("login-api.docx");
        attachmentRepository.save(attachment);

        List<Attachment> attachments = attachmentRepository.findAllByTaskId(savedTask.getId());

        assertNotNull(attachments);
        assertFalse(attachments.isEmpty(), "Expected at least one attachment");
        assertEquals("login-api.docx", attachments.get(0).getFileName());
        assertEquals(savedTask.getId(), attachments.get(0).getTask().getId());
    }
}
