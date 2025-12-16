package mate.academy.taskmanagementapp.dto.comment;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long userId;
    private Long taskId;
    private String text;
    private LocalDateTime timeStamp;
}
