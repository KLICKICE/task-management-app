package mate.academy.taskmanagementapp.repository;

import java.util.List;

import mate.academy.taskmanagementapp.model.comment.Comment;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUser(User user);

    List<Comment> findAllByTask(Task task);
}
