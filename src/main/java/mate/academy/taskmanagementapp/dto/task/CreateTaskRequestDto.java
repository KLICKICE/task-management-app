package mate.academy.taskmanagementapp.dto.task;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskRequestDto {
    @NotBlank
    private String title;

    @NotNull
    private Long assignedUserId;

    @NotNull
    private Long projectId;

    @Future
    private LocalDateTime deadline;
}
