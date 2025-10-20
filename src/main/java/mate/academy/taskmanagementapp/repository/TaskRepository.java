package mate.academy.taskmanagementapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import mate.academy.taskmanagementapp.model.Task;
import mate.academy.taskmanagementapp.model.TaskPriority;
import mate.academy.taskmanagementapp.model.TaskStatus;
import mate.academy.taskmanagementapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAssignedUser(User user);

    List<Task> findAllByStatus(TaskStatus status);

    List<Task> findAllByPriority(TaskPriority priority);

    List<Task> findAllByDeadlineBefore(LocalDateTime date);
}
