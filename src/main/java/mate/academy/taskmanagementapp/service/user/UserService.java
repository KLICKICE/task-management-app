package mate.academy.taskmanagementapp.service.user;

import mate.academy.taskmanagementapp.dto.user.ChangePasswordRequestDto;
import mate.academy.taskmanagementapp.dto.user.UserLoginDto;
import mate.academy.taskmanagementapp.dto.user.UserRegistrationDto;
import mate.academy.taskmanagementapp.dto.user.UserResponseDto;
import mate.academy.taskmanagementapp.dto.user.UserUpdateDto;
import mate.academy.taskmanagementapp.model.role.Role;

public interface UserService {
    UserResponseDto register(UserRegistrationDto userRegistrationDto);

    UserResponseDto findById(Long id);

    UserResponseDto login(UserLoginDto loginDto);

    UserResponseDto updateProfile(Long id, UserUpdateDto userUpdateDto);

    UserResponseDto changePassword(Long id, ChangePasswordRequestDto dto);

    void assignRole(Long id, Role.RoleName roleName);
}
