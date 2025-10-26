package mate.academy.taskmanagementapp.service.project;

import java.util.List;

import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;

public interface ProjectService {
    ProjectDto createProject(CreateProjectRequestDto dto);

    ProjectDto getProjectById(Long id);

    List<ProjectDto> findAllByOwner(Long ownerId);

    List<ProjectDto> findAllByStatus(ProjectStatus.StatusProject status);

    ProjectDto updateProject(Long id, ProjectUpdateDto dto);

    void deleteProject(Long id);
}
