package mate.academy.taskmanagementapp.service.user;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.user.ChangePasswordRequestDto;
import mate.academy.taskmanagementapp.dto.user.UserLoginDto;
import mate.academy.taskmanagementapp.dto.user.UserRegistrationDto;
import mate.academy.taskmanagementapp.dto.user.UserResponseDto;
import mate.academy.taskmanagementapp.dto.user.UserUpdateDto;
import mate.academy.taskmanagementapp.exception.AuthenticationException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.exception.RegistrationException;
import mate.academy.taskmanagementapp.mapper.UserMapper;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.RoleRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationDto dto) {
        String username = dto.getUsername();

        if (userRepository.existsByUsername(username)) {
            throw new RegistrationException("User with username='%s' already exists"
                    .formatted(username));
        }

        User user = userMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role userRole = roleRepository.findByRoleName(Role.RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Role with roleName=%s not found"
                        .formatted(Role.RoleName.USER)));

        user.setRoles(Set.of(userRole));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "User with id=%d not found".formatted(id)));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto login(UserLoginDto loginDto) {
        String username = loginDto.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username='%s' not found"
                        .formatted(username)));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AuthenticationException(
                    "Invalid password for username='%s'".formatted(username));
        }

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateProfile(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "User with id=%d not found".formatted(id)));

        userMapper.updateUserFromDto(userUpdateDto, user);

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDto changePassword(Long id, ChangePasswordRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id=%d not found".formatted(id)));

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new AuthenticationException(
                    "Current password is incorrect for userId=%d".formatted(id));
        }

        if (!dto.getNewPassword().equals(dto.getRepeatNewPassword())) {
            throw new AuthenticationException(
                    "New password and repeat do not match for userId=%d".formatted(id));
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void assignRole(Long id, Role.RoleName roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id=%d not found".formatted(id)));

        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Role with roleName=%s not found".formatted(roleName)));

        user.getRoles().add(role);
        userRepository.save(user);
    }
}
