package mate.academy.taskmanagementapp.repository;

import java.time.LocalDateTime;
import java.util.List;

import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.task.TaskPriority;
import mate.academy.taskmanagementapp.model.task.TaskStatus;
import mate.academy.taskmanagementapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByAssignedUser(User user);

    List<Task> findAllByStatus_StatusTask(TaskStatus.StatusTask status);

    List<Task> findAllByPriority_PriorityStatus(TaskPriority.PriorityStatus priority);

    List<Task> findAllByDeadlineBefore(LocalDateTime deadline);
}

