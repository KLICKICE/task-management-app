package mate.academy.taskmanagementapp.service.role;

import mate.academy.taskmanagementapp.model.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findByRoleName(Role.RoleName roleName);
}
