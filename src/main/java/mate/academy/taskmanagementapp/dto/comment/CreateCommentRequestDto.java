package mate.academy.taskmanagementapp.dto.comment;

import lombok.Data;

@Data
public class CreateCommentRequestDto {
    private Long taskId;
    private String text;
}
