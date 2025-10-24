package mate.academy.taskmanagementapp.service;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.model.Role;
import mate.academy.taskmanagementapp.model.User;
import mate.academy.taskmanagementapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceHelper {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new EntityNotFoundException("No authenticated user found");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: "
                        + email));
    }

    public boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == Role.RoleName.ADMIN);
    }
}
