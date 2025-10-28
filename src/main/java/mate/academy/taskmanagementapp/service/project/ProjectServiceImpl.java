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
import mate.academy.taskmanagementapp.model.project.ProjectStatus.StatusProject;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.ProjectRepository;
import mate.academy.taskmanagementapp.repository.ProjectStatusRepository;
import mate.academy.taskmanagementapp.service.AuthServiceHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectStatusRepository projectStatusRepository;
    private final AuthServiceHelper authServiceHelper;

    @Override
    public ProjectDto createProject(CreateProjectRequestDto dto) {
        User currentUser = authServiceHelper.getCurrentUser();

        Project entity = projectMapper.toEntity(dto);
        entity.setOwner(currentUser);
        entity.setCreatedAt(LocalDateTime.now());

        ProjectStatus status = projectStatusRepository
                .findByStatusProject(StatusProject.INITIATED)
                .orElseThrow(() -> new
                        EntityNotFoundException("Default status not found: INITIATED"));
        entity.setStatus(status);

        return projectMapper.toDto(projectRepository.save(entity));
    }

    @Override
    public List<ProjectDto> getUserProjects() {
        User currentUser = authServiceHelper.getCurrentUser();

        return projectRepository.findAllByOwnerId(currentUser.getId())
                .stream()
                .map(projectMapper::toDto)
                .toList();
    }

    @Override
    public ProjectDto getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        validateUserPermission(project, authServiceHelper.getCurrentUser());
        return projectMapper.toDto(project);
    }

    @Override
    public ProjectDto updateProject(Long id, ProjectUpdateDto dto) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        User currentUser = authServiceHelper.getCurrentUser();
        validateUserPermission(project, currentUser);

        projectMapper.updateProjectFromDto(dto, project);

        if (dto.getProjectStatus() != null) {
            ProjectStatus.StatusProject statusEnum;
            try {
                statusEnum = ProjectStatus
                        .StatusProject.valueOf(dto.getProjectStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new EntityNotFoundException("Invalid status value: "
                        + dto.getProjectStatus());
            }

            ProjectStatus newStatus = projectStatusRepository
                    .findByStatusProject(statusEnum)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Status not found: " + dto.getProjectStatus()));
            project.setStatus(newStatus);
        }

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
            throw new AccessDeniedException("You are not allowed to access or modify this project");
        }
    }
}
