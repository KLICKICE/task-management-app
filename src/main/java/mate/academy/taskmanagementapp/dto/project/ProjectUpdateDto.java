package mate.academy.taskmanagementapp.dto.project;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProjectUpdateDto {
    private String title;
    private Long ownerId;
    private String description;
    private LocalDateTime endDate;
    private String projectStatus;
}
