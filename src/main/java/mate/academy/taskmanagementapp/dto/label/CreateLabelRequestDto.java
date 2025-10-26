package mate.academy.taskmanagementapp.dto.label;

import lombok.Data;

@Data
public class CreateLabelRequestDto {
    private String name;
    private String color;
}
