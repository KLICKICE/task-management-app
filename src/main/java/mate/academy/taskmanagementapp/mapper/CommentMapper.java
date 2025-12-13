package mate.academy.taskmanagementapp.mapper;

import mate.academy.taskmanagementapp.config.MapConfig;
import mate.academy.taskmanagementapp.dto.comment.CommentDto;
import mate.academy.taskmanagementapp.dto.comment.CreateCommentRequestDto;
import mate.academy.taskmanagementapp.model.comment.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapConfig.class)
public interface CommentMapper {

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "userId", source = "user.id")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "timeStamp", ignore = true)
    Comment toEntity(CreateCommentRequestDto dto);
}

