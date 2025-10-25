package mate.academy.taskmanagementapp.repository;

import java.util.List;

import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.project.ProjectStatus;
import mate.academy.taskmanagementapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwner(User owner);

    List<Project> findAllByStatus(ProjectStatus.StatusProject status);
}
