package mate.academy.taskmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new task successfully")
    void createTask_success() throws Exception {
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto();
        createTaskRequestDto.setTitle("Test Task");
        createTaskRequestDto.setAssignedUserId(1L);

        MvcResult mvcResult = mockMvc.perform(
                        post("/api/tasks")
                                .content(toJson(createTaskRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        TaskDto taskDto = fromJson(mvcResult, TaskDto.class);

        assertNotNull(taskDto);
        assertEquals("Test Task", taskDto.getTitle());
        assertEquals("user@example.com", taskDto.getAssignedUserEmail());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get all tasks successfully")
    void getAllTasks_success() throws Exception {
        Long id = 1L;
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk());
    }



    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T fromJson(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }
}
