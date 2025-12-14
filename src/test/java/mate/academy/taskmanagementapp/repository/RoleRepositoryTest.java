package mate.academy.taskmanagementapp.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import mate.academy.taskmanagementapp.model.role.Role;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void cleanUp() {
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("""
            Save a role, success 
            """)
    void saveRole_success() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);

        Role savedRole = roleRepository.save(role);

        assertNotNull(savedRole.getId());
        assertEquals(Role.RoleName.USER, savedRole.getRoleName());
    }

    @Test
    @DisplayName("""
            Find role by RoleName successfully
            """)
    void findRole_ByRoleName_success() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.USER);
        roleRepository.saveAndFlush(role);

        Role foundRole = roleRepository.findByRoleName(Role.RoleName.USER)
                .orElseThrow(() -> new AssertionError("Role not found"));

        assertEquals(Role.RoleName.USER, foundRole.getRoleName());
    }
}