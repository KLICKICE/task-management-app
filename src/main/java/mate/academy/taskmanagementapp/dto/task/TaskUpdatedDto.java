package mate.academy.taskmanagementapp.dto.task;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskUpdatedDto {
    private String title;
    private String description;
    private LocalDateTime deadline;

    private String assignedUserEmail;

    private Long assignedUserId;

    private String taskStatus;
    private String taskPriority;
}
