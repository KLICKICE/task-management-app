package mate.academy.taskmanagementapp.service.project;

import java.util.List;

import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;

public interface ProjectService {
    ProjectDto createProject(CreateProjectRequestDto dto);

    List<ProjectDto> getUserProjects();

    ProjectDto getProjectById(Long id);

    ProjectDto updateProject(Long id, ProjectUpdateDto dto);

    void deleteProject(Long id);
}
