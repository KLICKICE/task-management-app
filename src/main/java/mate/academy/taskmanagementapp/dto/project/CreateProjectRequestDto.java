package mate.academy.taskmanagementapp.dto.project;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProjectRequestDto {
    @NotBlank
    private String title;

    @NotNull
    private Long ownerId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String description;
}
