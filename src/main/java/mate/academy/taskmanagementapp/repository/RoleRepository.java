package mate.academy.taskmanagementapp.repository;

import java.util.Optional;

import mate.academy.taskmanagementapp.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(Role.RoleName roleName);
}
