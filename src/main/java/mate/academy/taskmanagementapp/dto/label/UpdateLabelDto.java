package mate.academy.taskmanagementapp.dto.label;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UpdateLabelDto {
    private String name;
    private String color;
}
