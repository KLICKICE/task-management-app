package mate.academy.taskmanagementapp.dto.task;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String assignedUserEmail;
    private String taskStatus;
    private String taskPriority;
    private LocalDateTime deadline;
}

