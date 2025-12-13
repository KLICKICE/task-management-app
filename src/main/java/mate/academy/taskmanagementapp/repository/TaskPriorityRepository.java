package mate.academy.taskmanagementapp.repository;

import java.util.Optional;

import mate.academy.taskmanagementapp.model.task.TaskPriority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long> {
    Optional<TaskPriority> findByPriorityStatus(TaskPriority.PriorityStatus priorityStatus);
}
