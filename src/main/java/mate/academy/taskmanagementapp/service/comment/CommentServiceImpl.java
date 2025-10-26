package mate.academy.taskmanagementapp.service.comment;

import java.util.List;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.comment.CommentDto;
import mate.academy.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.CommentMapper;
import mate.academy.taskmanagementapp.model.comment.Comment;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.repository.CommentRepository;
import mate.academy.taskmanagementapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final TaskRepository taskRepository;

    @Override
    public CommentDto addComment(CreateCommentRequestDto requestDto) {
        Comment entity = commentMapper.toEntity(requestDto);
        return commentMapper.toDto(commentRepository.save(entity));
    }

    @Override
    public List<CommentDto> getCommentsByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        return commentRepository.findAllByTask(task)
                .stream().map(commentMapper::toDto).toList();
    }
}
