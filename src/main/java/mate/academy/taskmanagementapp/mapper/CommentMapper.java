package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.comment.CommentDto;
import mate.academy.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementapp.model.comment.Comment;
import org.mapstruct.Mapper;

@Mapper(config = MapConfig.class)
public interface CommentMapper {
    Comment toEntity(CreateCommentRequestDto createCommentRequestDto);

    CommentDto toDto(Comment comment);
}
