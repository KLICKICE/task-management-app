package mate.academy.taskmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;

import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Create a new project successfully")
    void createProject_success() throws Exception {
        Long id = 1L;

        CreateProjectRequestDto dto = new CreateProjectRequestDto();
        dto.setTitle("My First Project");
        dto.setOwnerId(id);
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(LocalDate.now().plusDays(7));

        MvcResult result = mockMvc.perform(post("/api/projects")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        ProjectDto actual = fromJson(result, ProjectDto.class);
        assertNotNull(actual);
        assertEquals("My First Project", actual.getTitle());
        assertEquals(id, actual.getOwnerId());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql",
                    "/testData/projects.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get a project successfully")
    void getProjectById() throws Exception {
        Long projectId = 1L;
        mockMvc.perform(get("/api/projects/{id}", projectId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql",
                    "/testData/projects.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get all projects from user successfully")
    void getUserProjects() throws Exception {
        mockMvc.perform(get("/api/projects/my-projects"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql",
                    "/testData/projects.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Update a project successfully")
    void updateProject() throws Exception {
        Long projectId = 1L;

        ProjectUpdateDto dto = new ProjectUpdateDto();
        dto.setTitle("Updated Project Title");
        dto.setProjectStatus("COMPLETED");
        dto.setEndDate(LocalDate.now().plusDays(10));

        MvcResult result = mockMvc.perform(put("/api/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto updated = fromJson(result, ProjectDto.class);
        assertEquals("Updated Project Title", updated.getTitle());
        assertEquals("COMPLETED", updated.getProjectStatus());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/insert_project_statuses.sql",
                    "/testData/projects.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Delete a project successfully")
    void deleteProject() throws Exception {
        Long projectId = 1L;
        mockMvc.perform(delete("/api/projects/{id}", projectId))
                .andExpect(status().isNoContent());
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T fromJson(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }
}
