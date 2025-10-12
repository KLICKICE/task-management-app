package mate.academy.taskmanagementapp.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class UserUpdateDto {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}
