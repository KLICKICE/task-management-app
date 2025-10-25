package mate.academy.taskmanagementapp.repository;

import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import mate.academy.taskmanagementapp.model.project.*;
import mate.academy.taskmanagementapp.model.user.*;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findAllByOwner(User owner);

    List<Project> findAllByStatus(ProjectStatus.StatusProject status);
}
