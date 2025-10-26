package mate.academy.taskmanagementapp.service.project;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
import mate.academy.taskmanagementapp.exception.AccessDeniedException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.ProjectMapper;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.ProjectRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final AuthServiceHelper authServiceHelper;

    @Override
    public ProjectDto createProject(CreateProjectRequestDto dto) {
        Project entity = projectMapper.toEntity(dto);
        User user = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        entity.setOwner(user);
        entity.setCreatedAt(LocalDateTime.now());
        return projectMapper.toDto(projectRepository.save(entity));
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        return projectMapper.toDto(project);
    }

    @Override
    public List<ProjectDto> findAllByOwner(Long ownerId) {
        return projectRepository.findAllByOwnerId(ownerId)
                .stream().map(projectMapper::toDto).toList();
    }

    @Override
    public List<ProjectDto> findAllByStatus(ProjectStatus.StatusProject status) {
        return projectRepository.findAllByStatus(status)
                .stream().map(projectMapper::toDto).toList();
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectUpdateDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(project, currentUser);
        projectMapper.updateProjectFromDto(dto, project);
        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(project, currentUser);
        projectRepository.delete(project);
    }

    private void validateUserPermission(Project project, User currentUser) {
        boolean isAdmin = authServiceHelper.isAdmin(currentUser);
        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You are not allowed to modify this task");
        }
    }
}
