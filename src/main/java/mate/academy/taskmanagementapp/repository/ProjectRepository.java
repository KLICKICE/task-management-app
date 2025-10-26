package mate.academy.taskmanagementapp.repository;

import java.util.List;

import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwnerId(Long owner);

    List<Project> findAllByStatus(ProjectStatus.StatusProject status);
}
