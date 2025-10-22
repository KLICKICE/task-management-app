package mate.academy.taskmanagementapp.dto.user;

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
