package mate.academy.taskmanagementapp.repository;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import mate.academy.taskmanagementapp.model.role.Role;
import mate.academy.taskmanagementapp.model.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Save a user successfully")
    void saveUser_success() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        Role savedRole = roleRepository.save(role);

        User user = new User();
        user.setFirstName("Sunless");
        user.setLastName("Leywin");
        user.setUsername("Lost from Light");
        user.setEmail("sunless@gmail.com");
        user.setPassword(passwordEncoder.encode("Sunless07062006"));
        user.setRoles(Set.of(savedRole));

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Sunless", savedUser.getFirstName());
        assertEquals("Leywin", savedUser.getLastName());
        assertEquals("Lost from Light", savedUser.getUsername());
        assertEquals("sunless@gmail.com", savedUser.getEmail());
        assertTrue(passwordEncoder.matches("Sunless07062006", savedUser.getPassword()));
        assertTrue(savedUser.getRoles().stream()
                .anyMatch(r -> r.getRoleName() == Role.RoleName.ADMIN));
    }

    @Test
    @DisplayName("""
            Find user by UserName, success
            """)
    void findUser_By_UserName() {
        Role role = new Role();
        role.setRoleName(Role.RoleName.ADMIN);
        Role savedRole = roleRepository.save(role);

        User user = new User();
        user.setFirstName("Sunless");
        user.setLastName("Leywin");
        user.setUsername("Lost from Light");
        user.setEmail("sunless@gmail.com");
        user.setPassword(passwordEncoder.encode("Sunless07062006"));
        user.setRoles(Set.of(savedRole));

        User savedUser = userRepository.save(user);

        assertTrue(userRepository.findByUsername(savedUser.getUsername()).isPresent());
    }
}
