package mate.academy.taskmanagementapp.dto.project;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ProjectUpdateDto {
    private String title;
    private Long ownerId;
    private String description;
    private LocalDate endDate;
    private String projectStatus;
}
