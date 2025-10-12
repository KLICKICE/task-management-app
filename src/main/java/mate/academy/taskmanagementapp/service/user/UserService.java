package mate.academy.taskmanagementapp.service.user;

import mate.academy.taskmanagementapp.dto.*;
import mate.academy.taskmanagementapp.model.*;

import java.util.*;

public interface UserService {
    UserResponseDto register(UserRegistrationDto userRegistrationDto);
    UserResponseDto login(UserLoginDto loginDto);

    Optional<User> findById(Long id);

    User updateProfile(Long id, UserUpdateDto userUpdateDto);

    void assignRole(Long id, Role.RoleName roleName);
}
