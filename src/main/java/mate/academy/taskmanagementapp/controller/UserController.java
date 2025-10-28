package mate.academy.taskmanagementapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.user.UserLoginDto;
import mate.academy.taskmanagementapp.dto.user.UserRegistrationDto;
import mate.academy.taskmanagementapp.dto.user.UserResponseDto;
import mate.academy.taskmanagementapp.dto.user.UserUpdateDto;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.UserRepository;
import mate.academy.taskmanagementapp.service.user.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User & Auth management", description = "Authentication and user profile operations")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/auth/register")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationDto dto) {
        return userService.register(dto);
    }

    @PostMapping("/auth/login")
    public UserResponseDto login(@Valid @RequestBody UserLoginDto dto) {
        return userService.login(dto);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/users/me")
    public UserResponseDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userService.findById(currentUser.getId());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/users/me")
    public UserResponseDto updateCurrentUser(@Valid @RequestBody UserUpdateDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userService.updateProfile(currentUser.getId(), dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/users/{id}/role")
    public void updateUserRole(@PathVariable Long id, @RequestParam Role.RoleName roleName) {
        userService.assignRole(id, roleName);
    }
}
