package mate.academy.taskmanagementapp.service.role;

import lombok.*;
import mate.academy.taskmanagementapp.model.Role;
import mate.academy.taskmanagementapp.repository.*;
import org.springframework.stereotype.*;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByRoleName(Role.RoleName roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
