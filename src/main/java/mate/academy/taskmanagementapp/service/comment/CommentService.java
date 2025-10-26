package mate.academy.taskmanagementapp.service.comment;

import java.util.List;

import mate.academy.taskmanagementapp.dto.comment.CommentDto;
import mate.academy.taskmanagementapp.dto.comment.CreateCommentRequestDto;

public interface CommentService {
    CommentDto addComment(CreateCommentRequestDto requestDto);

    List<CommentDto> getCommentsByTaskId(Long taskId);
}
