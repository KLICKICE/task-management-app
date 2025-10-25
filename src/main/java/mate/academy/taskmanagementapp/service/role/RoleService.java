package mate.academy.taskmanagementapp.service.role;

import java.util.Optional;

import mate.academy.taskmanagementapp.model.role.Role;

public interface RoleService {
    Optional<Role> findByRoleName(Role.RoleName roleName);
}
