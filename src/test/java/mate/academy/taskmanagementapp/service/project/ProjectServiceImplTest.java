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
import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
import mate.academy.taskmanagementapp.mapper.ProjectMapper;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.ProjectRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private AuthServiceHelper authServiceHelper;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private User user;
    private Role role;
    private Project project;
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
        user.setRoles(Set.of(role));

        project = new Project();
        project.setId(1L);
        project.setTitle("Shadowfall System");
        project.setDescription("Born from darkness and persistence.");
        project.setOwner(user);
        project.setStatus(ProjectStatus.INITIATED);

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
    @DisplayName("Context loads")
    void contextLoads() {
        assertNotNull(projectService);
        assertNotNull(projectRepository);
        assertNotNull(authServiceHelper);
    }

    @Test
    @DisplayName("Create project success")
    void createProject_success() {
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectMapper.toEntity(createProjectRequestDto)).thenReturn(project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto created = projectService.createProject(createProjectRequestDto);

        assertNotNull(created);
        assertEquals("Shadowfall System", created.getTitle());

        verify(authServiceHelper).getCurrentUser();
        verify(projectMapper).toEntity(createProjectRequestDto);
        verify(projectRepository).save(project);
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName("Get user projects success")
    void getUserProjects_success() {
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectRepository.findAllByOwnerId(user.getId())).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        List<ProjectDto> projects = projectService.getUserProjects();

        assertNotNull(projects);
        assertEquals(1, projects.size());

        verify(authServiceHelper).getCurrentUser();
        verify(projectRepository).findAllByOwnerId(user.getId());
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName("Get project by id success")
    void getProjectById_success() {
        Long id = 1L;

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto result = projectService.getProjectById(id);

        assertNotNull(result);
        assertEquals("Shadowfall System", result.getTitle());

        verify(projectRepository).findById(id);
        verify(authServiceHelper).getCurrentUser();
        verify(projectMapper).toDto(project);
    }

    @Test
    @DisplayName("Update project success")
    void updateProject_success() {
        Long id = 1L;

        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(authServiceHelper.getCurrentUser()).thenReturn(user);
        doNothing().when(projectMapper).updateProjectFromDto(projectUpdateDto, project);
        when(projectRepository.save(project)).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(projectDto);

        ProjectDto updated = projectService.updateProject(id, projectUpdateDto);

        assertNotNull(updated);
        assertEquals("Shadowfall System", updated.getTitle());

        verify(projectRepository).findById(id);
        verify(authServiceHelper).getCurrentUser();
        verify(projectMapper).updateProjectFromDto(projectUpdateDto, project);
        verify(projectRepository).save(project);
        verify(projectMapper).toDto(project);
    }
}
