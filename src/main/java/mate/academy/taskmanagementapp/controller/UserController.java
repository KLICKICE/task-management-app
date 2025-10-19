package mate.academy.taskmanagementapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.dto.UserLoginDto;
import mate.academy.taskmanagementapp.dto.UserRegistrationDto;
import mate.academy.taskmanagementapp.dto.UserResponseDto;
import mate.academy.taskmanagementapp.dto.UserUpdateDto;
import mate.academy.taskmanagementapp.exception.AccessDeniedException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.model.Role;
import mate.academy.taskmanagementapp.model.User;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Operations related to users")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/registration")
    public UserResponseDto register(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.register(userRegistrationDto);
    }

    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody UserLoginDto userLoginDto) {
        return userService.login(userLoginDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{id}")
    public UserResponseDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/users/{id}")
    public UserResponseDto updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserUpdateDto userUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentName = authentication.getName();

        User currentUser = userRepository.findByUsername(currentName)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!currentUser.getId().equals(id)
                && currentUser.getRoles().stream()
                .noneMatch(r -> r.getRoleName() == Role.RoleName.ADMIN)) {
            throw new AccessDeniedException("You can update only your own profile");
        }

        return userService.updateProfile(id, userUpdateDto);
    }
}
