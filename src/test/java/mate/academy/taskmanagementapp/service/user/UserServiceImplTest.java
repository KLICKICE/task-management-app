package mate.academy.taskmanagementapp.service.user;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import mate.academy.taskmanagementapp.dto.user.UserLoginDto;
import mate.academy.taskmanagementapp.dto.user.UserRegistrationDto;
import mate.academy.taskmanagementapp.dto.user.UserResponseDto;
import mate.academy.taskmanagementapp.dto.user.UserUpdateDto;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.mapper.UserMapper;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.RoleRepository;
import mate.academy.taskmanagementapp.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Role userRole;
    private Role adminRole;
    private UserRegistrationDto registrationDto;
    private UserResponseDto responseDto;
    private UserLoginDto loginDto;
    private UserUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setRoleName(Role.RoleName.USER);

        adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);

        user = new User();
        user.setId(1L);
        user.setUsername("LostFromLight");
        user.setPassword("encodedPassword");
        user.setRoles(new java.util.HashSet<>());
        user.getRoles().add(userRole);

        registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("LostFromLight");
        registrationDto.setPassword("rawPassword");

        responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setUsername("LostFromLight");

        loginDto = new UserLoginDto();
        loginDto.setUsername("LostFromLight");
        loginDto.setPassword("rawPassword");

        updateDto = new UserUpdateDto();
        updateDto.setUsername("FoundWithLight");
    }

    @Test
    @DisplayName("Register new user successfully")
    void registerUser_Success() {
        when(userRepository.existsByUsername(registrationDto.getUsername())).thenReturn(false);
        when(userMapper.toEntity(registrationDto)).thenReturn(user);
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByRoleName(Role.RoleName.USER)).thenReturn(Optional.of(userRole));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto actual = userService.register(registrationDto);

        assertNotNull(actual);
        assertEquals("LostFromLight", actual.getUsername());
        verify(userRepository).existsByUsername("LostFromLight");
        verify(userMapper).toEntity(registrationDto);
        verify(passwordEncoder).encode("rawPassword");
        verify(roleRepository).findByRoleName(Role.RoleName.USER);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Find user by Id successfully")
    void findUserBy_Id_Success() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto actual = userService.findById(id);

        assertNotNull(actual);
        assertEquals("LostFromLight", actual.getUsername());

        verify(userRepository).findById(id);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Find user by Id throws exception when not found")
    void findUserBy_Id_NotFound() {
        Long id = 99L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(id)
        );

        assertEquals("User with id=99 not found", exception.getMessage());
        verify(userRepository).findById(id);
        verify(userMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("Login user successfully")
    void login_Success() {
        when(userRepository.findByUsername("LostFromLight")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto actual = userService.login(loginDto);

        assertNotNull(actual);
        assertEquals("LostFromLight", actual.getUsername());
        verify(userRepository).findByUsername("LostFromLight");
        verify(passwordEncoder).matches("rawPassword", "encodedPassword");
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Update user profile successfully")
    void update_Success() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromDto(updateDto, user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(responseDto);

        UserResponseDto actual = userService.updateProfile(id, updateDto);

        assertNotNull(actual);
        assertEquals("LostFromLight", actual.getUsername());

        verify(userRepository).findById(id);
        verify(userMapper).updateUserFromDto(updateDto, user);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Assign role successfully")
    void assignRole_Success() {
        Role adminRole = new Role();
        adminRole.setRoleName(Role.RoleName.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleName(Role.RoleName.ADMIN)).thenReturn(Optional.of(adminRole));
        when(userRepository.save(user)).thenReturn(user);

        userService.assignRole(1L, Role.RoleName.ADMIN);

        assertNotNull(user.getRoles());
        assertEquals(true, user.getRoles().stream().anyMatch(r -> r.getRoleName() == Role.RoleName.ADMIN));

        verify(userRepository).findById(1L);
        verify(roleRepository).findByRoleName(Role.RoleName.ADMIN);
        verify(userRepository).save(user);
    }
}
