package mate.academy.taskmanagementapp.service.role;

import java.util.Optional;

import mate.academy.taskmanagementapp.model.Role;

public interface RoleService {
    Optional<Role> findByRoleName(Role.RoleName roleName);
}
