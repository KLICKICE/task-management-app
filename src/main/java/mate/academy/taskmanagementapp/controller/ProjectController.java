package mate.academy.taskmanagementapp.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.service.project.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Project management", description = "Operations related to projects")
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    ProjectDto createProject(@RequestBody CreateProjectRequestDto dto) {
        return projectService.createProject(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/{id}")
    ProjectDto getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/by-owner")
    List<ProjectDto> findAllByOwner(@RequestParam Long ownerId) {
        return projectService.findAllByOwner(ownerId);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/by-status")
    List<ProjectDto> findAllByStatus(@RequestParam ProjectStatus.StatusProject status) {
        return projectService.findAllByStatus(status);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/{id}")
    ProjectDto updateProject(@PathVariable Long id,
                                    @RequestBody ProjectUpdateDto dto) {
        return projectService.updateProject(id, dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/{id}")
    void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }
}
