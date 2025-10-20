package mate.academy.taskmanagementapp.service.user;

import mate.academy.taskmanagementapp.dto.UserLoginDto;
import mate.academy.taskmanagementapp.dto.UserRegistrationDto;
import mate.academy.taskmanagementapp.dto.UserResponseDto;
import mate.academy.taskmanagementapp.dto.UserUpdateDto;
import mate.academy.taskmanagementapp.model.Role;

public interface UserService {
    UserResponseDto register(UserRegistrationDto userRegistrationDto);

    UserResponseDto login(UserLoginDto loginDto);

    UserResponseDto findById(Long id);

    UserResponseDto updateProfile(Long id, UserUpdateDto userUpdateDto);

    void assignRole(Long id, Role.RoleName roleName);
}
