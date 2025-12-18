package mate.academy.taskmanagementapp.service.project;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
import mate.academy.taskmanagementapp.mapper.ProjectMapper;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.ProjectRepository;
import mate.academy.taskmanagementapp.repository.ProjectStatusRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProjectStatusRepository projectStatusRepository;

    @Mock
    private AuthServiceHelper authServiceHelper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private User user;
    private Role role;
    private Project project;
    private ProjectStatus projectStatus;
    private CreateProjectRequestDto createProjectRequestDto;
    private ProjectDto projectDto;
    private ProjectUpdateDto projectUpdateDto;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.USER);

        user = new User();
        user.setId(1L);
        user.setUsername("LostFromLight");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(role));

        project = new Project();
        project.setId(1L);
        project.setTitle("Shadowfall System");
        project.setDescription("Born from darkness and persistence.");
        project.setOwner(user);

        projectStatus = new ProjectStatus();
        projectStatus.setId(1L);
        projectStatus.setStatusProject(ProjectStatus.StatusProject.INITIATED);

        createProjectRequestDto = new CreateProjectRequestDto();
        createProjectRequestDto.setTitle("Shadowfall System");

        projectDto = new ProjectDto();
        projectDto.setId(1L);
        projectDto.setTitle("Shadowfall System");

        projectUpdateDto = new ProjectUpdateDto();
        projectUpdateDto.setTitle("Updated Shadowfall");
        projectUpdateDto.setDescription("Rising from light and code.");
        projectUpdateDto.setProjectStatus("IN_PROGRESS");
    }

    @Test
    @DisplayName("Dummy sanity test for structure")
    void contextLoads() {
        assertNotNull(projectService);
        assertNotNull(projectRepository);
        assertNotNull(authServiceHelper);
    }

    @Test
    @DisplayName("Create project, success")
    void createProject_success() {
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectMapper.toEntity(createProjectRequestDto)).thenReturn(project);
        when(projectStatusRepository.findByStatusProject(ProjectStatus.StatusProject.INITIATED))
                .thenReturn(Optional.of(projectStatus));
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto createdProject = projectService.createProject(createProjectRequestDto);

        assertNotNull(createdProject);
        assertEquals("Shadowfall System", createdProject.getTitle());

        verify(authServiceHelper).getCurrentUser();
        verify(projectMapper).toEntity(createProjectRequestDto);
        verify(projectStatusRepository).findByStatusProject(ProjectStatus.StatusProject.INITIATED);
        verify(projectRepository).save(project);
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName("Get all users projects, success")
    void getAllProjects_ByUser_success() {
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectRepository.findAllByOwnerId(user.getId())).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        List<ProjectDto> userProjects = projectService.getUserProjects();

        assertNotNull(userProjects);

        verify(authServiceHelper).getCurrentUser();
        verify(projectRepository).findAllByOwnerId(user.getId());
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName("Get project by Id, success")
    void getProject_ById_success() {
        Long id = 1L;

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto actual = projectService.getProjectById(id);

        assertNotNull(actual);
        assertEquals(project.getTitle(), actual.getTitle());
        assertEquals("Shadowfall System", actual.getTitle());

        verify(projectRepository).findById(id);
        verify(authServiceHelper).getCurrentUser();
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName("Update Project, success")
    void updateProject_success() {
        Long id = 1L;

        ProjectStatus inProgressStatus = new ProjectStatus();
        inProgressStatus.setStatusProject(ProjectStatus.StatusProject.IN_PROGRESS);

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectStatusRepository.findByStatusProject(ProjectStatus.StatusProject.IN_PROGRESS))
                .thenReturn(Optional.of(inProgressStatus));

        doNothing().when(projectMapper).updateProjectFromDto(projectUpdateDto, project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto actual = projectService.updateProject(id, projectUpdateDto);

        assertNotNull(actual);
        assertEquals("Shadowfall System", actual.getTitle());

        verify(projectRepository).findById(id);
        verify(authServiceHelper).getCurrentUser();
        verify(projectStatusRepository).findByStatusProject(ProjectStatus.StatusProject.IN_PROGRESS);
        verify(projectMapper).updateProjectFromDto(projectUpdateDto, project);
        verify(projectRepository).save(project);
        verify(projectMapper).toDto(project);
    }
}
