package mate.academy.taskmanagementapp.dto.project;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProjectDto {
    private Long id;
    private String title;
    private String projectStatus;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private Long ownerId;
    private String description;
    private LocalDateTime endDate;
}
