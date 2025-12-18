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
import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final AuthServiceHelper authServiceHelper;

    @Override
    @Transactional
    public ProjectDto createProject(CreateProjectRequestDto dto) {
        User currentUser = authServiceHelper.getCurrentUser();

        Project entity = projectMapper.toEntity(dto);
        entity.setOwner(currentUser);
        entity.setCreatedAt(LocalDateTime.now());

        entity.setStatus(ProjectStatus.INITIATED);

        return projectMapper.toDto(projectRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDto> getUserProjects() {
        User currentUser = authServiceHelper.getCurrentUser();

        return projectRepository.findAllByOwnerId(currentUser.getId())
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: id=" + id));

        validateUserPermission(project, authServiceHelper.getCurrentUser());
        return projectMapper.toDto(project);
    }

    @Override
    @Transactional
    public ProjectDto updateProject(Long id, ProjectUpdateDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: id=" + id));

        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(project, currentUser);

        projectMapper.updateProjectFromDto(dto, project);

        if (dto.getProjectStatus() != null && !dto.getProjectStatus().isBlank()) {
            project.setStatus(resolveStatus(dto.getProjectStatus()));
        }

        return projectMapper.toDto(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: id=" + id));

        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(project, currentUser);

        projectRepository.delete(project);
    }

    private void validateUserPermission(Project project, User currentUser) {
        boolean isAdmin = authServiceHelper.isAdmin(currentUser);
        boolean isOwner = project.getOwner() != null
                && project.getOwner().getId() != null
                && project.getOwner().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException(
                    "You are not allowed to access or modify projectId=" + project.getId()
            );
        }
    }

    private ProjectStatus resolveStatus(String raw) {
        try {
            return ProjectStatus.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Invalid project status: status=" + raw);
        }
    }
}
