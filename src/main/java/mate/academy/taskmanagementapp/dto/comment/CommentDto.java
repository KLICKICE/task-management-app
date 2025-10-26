package mate.academy.taskmanagementapp.dto.comment;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long userId;
    private Long taskId;
    private String text;
}
