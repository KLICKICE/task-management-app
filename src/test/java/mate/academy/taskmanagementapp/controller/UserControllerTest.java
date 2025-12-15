package mate.academy.taskmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import mate.academy.taskmanagementapp.dto.user.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(
            scripts = {"/testData/clean.sql", "/testData/insert_roles.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Register a new user successfully")
    void registerUser_ValidRequestDto_success() throws Exception {
        UserRegistrationDto request = new UserRegistrationDto();
        request.setUsername("john_doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");

        MvcResult result = mockMvc.perform(
                        post("/api/auth/register")
                                .content(toJson(request))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserResponseDto actual = fromJson(result, UserResponseDto.class);

        assertNotNull(actual);
        assertEquals("john_doe", actual.getUsername());
        assertEquals("john@example.com", actual.getEmail());
        assertNotNull(actual.getId());
    }

    @Test
    @Sql(
            scripts = {"/testData/clean.sql", "/testData/insert_roles.sql", "/testData/insert_users.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Login existing user successfully from SQL seed data")
    void loginUser_ExistingUserFromSql_success() throws Exception {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setUsername("user@example.com");
        loginDto.setPassword("password123");

        MvcResult result = mockMvc.perform(
                        post("/api/auth/login")
                                .content(toJson(loginDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserLoginResponseDto actual = fromJson(result, UserLoginResponseDto.class);

        assertNotNull(actual);
        assertNotNull(actual.token());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {"/testData/clean.sql", "/testData/insert_roles.sql", "/testData/insert_users.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get current user successfully")
    void getCurrentUser_success() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {"/testData/clean.sql", "/testData/insert_roles.sql", "/testData/insert_users.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Update an user successfully")
    void updateUser_success() throws Exception {
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setFirstName("UpdatedFirst");
        updateDto.setLastName("UpdatedLast");
        updateDto.setEmail("updated@example.com");

        mockMvc.perform(
                        put("/api/users/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(updateDto))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {"/testData/clean.sql", "/testData/insert_roles.sql", "/testData/insert_users.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Change password successfully")
    void changePassword_success() throws Exception {
        ChangePasswordRequestDto dto = new ChangePasswordRequestDto();
        dto.setCurrentPassword("password123");
        dto.setNewPassword("newPassword123");
        dto.setRepeatNewPassword("newPassword123");

        mockMvc.perform(
                        patch("/api/users/me/password")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(dto))
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"ADMIN"})
    @Sql(
            scripts = {"/testData/clean.sql", "/testData/insert_roles.sql", "/testData/insert_users.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Update a role for user successfully")
    void updateRoleForUser_success() throws Exception {
        Long userId = 1L;

        mockMvc.perform(
                        put("/api/users/{id}/role", userId)
                                .param("roleName", "ADMIN")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T fromJson(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }
}
