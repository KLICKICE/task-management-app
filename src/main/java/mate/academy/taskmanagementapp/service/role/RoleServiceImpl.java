package mate.academy.taskmanagementapp.service.role;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.taskmanagementapp.model.Role;
import mate.academy.taskmanagementapp.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByRoleName(Role.RoleName roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
