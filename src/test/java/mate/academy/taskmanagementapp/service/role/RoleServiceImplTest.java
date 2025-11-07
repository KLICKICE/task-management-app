package mate.academy.taskmanagementapp.service.role;

import java.util.Optional;

import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
    }

    @Test
    @DisplayName("Find role by RoleName successfully")
    void findByRoleName_Success() {
        // given
        when(roleRepository.findByRoleName(Role.RoleName.ADMIN))
                .thenReturn(Optional.of(role));

        // when
        Optional<Role> result = roleService.findByRoleName(Role.RoleName.ADMIN);

        // then
        assertTrue(result.isPresent());
        assertEquals(Role.RoleName.ADMIN, result.get().getRoleName());
        verify(roleRepository, times(1)).findByRoleName(Role.RoleName.ADMIN);
    }

    @Test
    @DisplayName("Find role by RoleName returns empty when not found")
    void findByRoleName_NotFound() {
        // given
        when(roleRepository.findByRoleName(Role.RoleName.USER))
                .thenReturn(Optional.empty());

        // when
        Optional<Role> result = roleService.findByRoleName(Role.RoleName.USER);

        // then
        assertTrue(result.isEmpty());
        verify(roleRepository, times(1)).findByRoleName(Role.RoleName.USER);
    }
}
