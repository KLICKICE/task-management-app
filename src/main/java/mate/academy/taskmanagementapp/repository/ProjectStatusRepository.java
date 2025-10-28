package mate.academy.taskmanagementapp.repository;

import java.util.Optional;

import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {
    Optional<ProjectStatus> findByStatusProject(ProjectStatus.StatusProject statusProject);
}

