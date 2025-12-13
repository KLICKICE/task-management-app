package mate.academy.taskmanagementapp.repository;

import java.time.LocalDate;
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
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.user.User;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

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

    private User savedUser;
    private ProjectStatus savedStatus;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        Role savedRole = roleRepository.save(role);

        User user = new User();
        user.setFirstName("Sunless");
        user.setLastName("Leywin");
        user.setUsername("Lost from Light");
        user.setEmail("sunless@gmail.com");
        user.setPassword(passwordEncoder.encode("Sunless07062006"));
        user.setRoles(Set.of(savedRole));
        savedUser = userRepository.save(user);

        ProjectStatus status = new ProjectStatus();
        status.setStatusProject(ProjectStatus.StatusProject.INITIATED);
        savedStatus = projectStatusRepository.save(status);
    }

    @Test
    @DisplayName("Save a project successfully")
    void saveProject_success() {
        Project project = new Project();
        project.setTitle("Shadowfall System");
        project.setDescription("A project born from darkness and persistence.");
        project.setOwner(savedUser);
        project.setStatus(savedStatus);
        project.setStartDate(LocalDate.now());

        Project savedProject = projectRepository.save(project);

        assertNotNull(savedProject.getId());
        assertEquals("Shadowfall System", savedProject.getTitle());
        assertEquals(savedUser.getId(), savedProject.getOwner().getId());
        assertEquals(savedStatus.getStatusProject(), savedProject.getStatus().getStatusProject());
        assertNotNull(savedProject.getCreatedAt());
    }

    @Test
    @DisplayName("Find projects by Owner Id successfully")
    void findProject_By_Owner_Id_success() {
        Project project = new Project();
        project.setTitle("Shadowfall System");
        project.setDescription("Born from darkness and persistence.");
        project.setOwner(savedUser);
        project.setStatus(savedStatus);
        project.setStartDate(LocalDate.now());
        projectRepository.save(project);

        List<Project> projects = projectRepository.findAllByOwnerId(savedUser.getId());

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals(savedUser.getId(), projects.get(0).getOwner().getId());
    }

    @Test
    @DisplayName("Find projects by Status successfully")
    void findProject_By_Status_success() {
        Project project = new Project();
        project.setTitle("Shadowfall System");
        project.setDescription("Born from darkness and persistence.");
        project.setOwner(savedUser);
        project.setStatus(savedStatus);
        project.setStartDate(LocalDate.now());
        projectRepository.save(project);

        List<Project> projects = projectRepository.findAllByStatus_StatusProject(ProjectStatus.StatusProject.INITIATED);

        assertNotNull(projects);
        assertFalse(projects.isEmpty());
        assertEquals(ProjectStatus.StatusProject.INITIATED, projects.get(0).getStatus().getStatusProject());
    }
}
