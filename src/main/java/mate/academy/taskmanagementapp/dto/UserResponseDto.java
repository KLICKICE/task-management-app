package mate.academy.taskmanagementapp.dto;

import lombok.*;
import mate.academy.taskmanagementapp.model.Role;

import java.util.Set;

@Data
@EqualsAndHashCode
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
}

