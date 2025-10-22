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

    List<Task> findAllByStatus(TaskStatus.StatusTask status);

    List<Task> findAllByPriority(TaskPriority.PriorityStatus priority);

    List<Task> findAllByDeadlineBefore(LocalDateTime deadline);
}
