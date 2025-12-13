package mate.academy.taskmanagementapp.repository;

import java.util.Optional;

import mate.academy.taskmanagementapp.model.task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    Optional<TaskStatus> findByStatusTask(TaskStatus.StatusTask statusTask);
}

