package mate.academy.taskmanagementapp.dto.task;

import lombok.Data;

@Data
public class TaskUpdatedDto {
    private String title;
    private String assignedUserEmail;
    private String taskStatus;
    private String taskPriority;
}
