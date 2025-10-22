package mate.academy.taskmanagementapp.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskRequestDto {
    @NotBlank
    private String title;

    @NotNull
    private Long assignedUserId;
}

