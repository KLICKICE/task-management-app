package mate.academy.taskmanagementapp.service.user;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.UserLoginDto;
import mate.academy.taskmanagementapp.dto.UserRegistrationDto;
import mate.academy.taskmanagementapp.dto.UserResponseDto;
import mate.academy.taskmanagementapp.dto.UserUpdateDto;
import mate.academy.taskmanagementapp.exception.AuthenticationException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.exception.RegistrationException;
import mate.academy.taskmanagementapp.mapper.UserMapper;
import mate.academy.taskmanagementapp.model.Role;
import mate.academy.taskmanagementapp.model.User;
import mate.academy.taskmanagementapp.repository.RoleRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new RegistrationException(
                    "User with username '" + userRegistrationDto.getUsername() + "' already exists"
            );
        }

        User user = userMapper.toEntity(userRegistrationDto);
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));

        Role userRole = roleRepository.findByRoleName(Role.RoleName.USER)
                .orElseThrow(()
                        -> new EntityNotFoundException("Role "
                        + Role.RoleName.USER + " not found"));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserResponseDto login(UserLoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new AuthenticationException("Invalid password");
        }

        return userMapper.toDto(user);
    }

    @Override
    public User updateProfile(Long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("Can't find user by id: " + id));
        userMapper.updateUserFromDto(userUpdateDto, user);
        return userRepository.save(user);
    }

    @Override
    public void assignRole(Long id, Role.RoleName roleName) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
