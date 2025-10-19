package mate.academy.taskmanagementapp.dto;

import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mate.academy.taskmanagementapp.model.Role;

@Data
@EqualsAndHashCode
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
}

