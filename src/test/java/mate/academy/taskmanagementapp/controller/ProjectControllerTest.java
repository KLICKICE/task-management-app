package mate.academy.taskmanagementapp.controller;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import mate.academy.taskmanagementapp.dto.project.CreateProjectRequestDto;
import mate.academy.taskmanagementapp.dto.project.ProjectDto;
import mate.academy.taskmanagementapp.dto.project.ProjectUpdateDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new project successfully")
    void createProject_success() throws Exception {
        Long id = 1L;
        CreateProjectRequestDto createProjectRequestDto = new CreateProjectRequestDto();
        createProjectRequestDto.setTitle("My First Project");
        createProjectRequestDto.setOwnerId(id);
        createProjectRequestDto.setStartDate(LocalDate.now());
        createProjectRequestDto.setEndDate(LocalDate.now().plusDays(7));


        MvcResult result = mockMvc.perform(post("/api/projects")
                        .content(toJson(createProjectRequestDto))
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
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql",
            "/testData/projects.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get a project successfully")
    void getProjectById() throws Exception {
        Long projectId = 1L;
        mockMvc.perform(get("/api/projects/{id}", projectId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql",
            "/testData/projects.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get all projects from user successfully")
    void getUserProjects() throws Exception {
        mockMvc.perform(get("/api/projects/my-projects"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql",
            "/testData/projects.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Update a project successfully")
    void updateProject() throws Exception {
        Long projectId = 1L;

        ProjectUpdateDto projectUpdateDto = new ProjectUpdateDto();
        projectUpdateDto.setTitle("Updated Project Title");
        projectUpdateDto.setProjectStatus("COMPLETED");
        projectUpdateDto.setEndDate(LocalDate.now().plusDays(10));

        MvcResult result = mockMvc.perform(put("/api/projects/{id}", projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(projectUpdateDto)))
                .andExpect(status().isOk())
                .andReturn();

        ProjectDto updatedProject = fromJson(result, ProjectDto.class);
        assertEquals("Updated Project Title", updatedProject.getTitle());
        assertEquals("COMPLETED", updatedProject.getProjectStatus());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql",
            "/testData/projects.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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