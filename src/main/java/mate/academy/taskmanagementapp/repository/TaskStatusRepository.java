package mate.academy.taskmanagementapp.repository;

import java.util.List;

import mate.academy.taskmanagementapp.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    List<TaskStatus> findByStatusTask(TaskStatus.StatusTask statusTask);
}

