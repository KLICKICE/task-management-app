package mate.academy.taskmanagementapp.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProjectRequestDto {
    @NotBlank
    private String title;

    @NotNull
    private Long ownerId;
}
