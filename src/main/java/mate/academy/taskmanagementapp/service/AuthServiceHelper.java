package mate.academy.taskmanagementapp.service;

import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.exception.AccessDeniedException;
import mate.academy.taskmanagementapp.exception.EntityNotFoundException;
import mate.academy.taskmanagementapp.model.project.Project;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.task.Task;
import mate.academy.taskmanagementapp.model.user.User;
import mate.academy.taskmanagementapp.repository.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceHelper {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            throw new EntityNotFoundException("No authenticated user found");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: "
                        + username));
    }

    public void assertCanAccessTask(Task task) {
        User current = getCurrentUser();

        if (isAdmin(current)) {
            return;
        }

        if (task.getAssignedUser() != null && task
                .getAssignedUser().getId().equals(current.getId())) {
            return;
        }

        Project project = task.getProject();
        if (project != null && project.getOwner() != null
                && project.getOwner().getId().equals(current.getId())) {
            return;
        }

        throw new AccessDeniedException("You don't have access to this task");
    }

    public boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getRoleName() == Role.RoleName.ADMIN);
    }
}
