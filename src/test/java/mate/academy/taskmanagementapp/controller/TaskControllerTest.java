package mate.academy.taskmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import mate.academy.taskmanagementapp.dto.label.LabelDto;
import mate.academy.taskmanagementapp.dto.task.CreateTaskRequestDto;
import mate.academy.taskmanagementapp.dto.task.TaskDto;
import mate.academy.taskmanagementapp.dto.task.TaskUpdatedDto;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
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
class TaskControllerTest {

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
                    "/testData/projects.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Create a new task successfully")
    void createTask_success() throws Exception {
        CreateTaskRequestDto dto = new CreateTaskRequestDto();
        dto.setTitle("Test Task");
        dto.setAssignedUserId(1L);
        dto.setProjectId(1L);


        MvcResult mvcResult = mockMvc.perform(
                        post("/tasks")
                                .content(toJson(dto))
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
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/projects.sql",
                    "/testData/insert_task.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Update task successfully")
    void updateTask_success() throws Exception {
        Long id = 1L;

        TaskUpdatedDto dto = new TaskUpdatedDto();
        dto.setTaskPriority("HIGH");
        dto.setTitle("Test Task");
        dto.setTaskStatus("DONE");
        dto.setAssignedUserEmail("user@example.com");

        MvcResult mvcResult = mockMvc.perform(put("/tasks/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto updatedDto = fromJson(mvcResult, TaskDto.class);
        assertEquals("Test Task", updatedDto.getTitle());
        assertEquals("High", updatedDto.getTaskPriority());
        assertEquals("DONE", updatedDto.getTaskStatus());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/projects.sql",
                    "/testData/insert_task.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Delete task successfully")
    void deleteTask_success() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/tasks/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/projects.sql",
                    "/testData/insert_task.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get tasks by projectId successfully")
    void getTasksByProjectId_success() throws Exception {
        Long projectId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/tasks")
                                .param("projectId", String.valueOf(projectId))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        TaskDto[] tasksArray = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                TaskDto[].class
        );

        List<TaskDto> tasks = Arrays.asList(tasksArray);

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty(), "Expected tasks list not to be empty");
        assertNotNull(tasks.get(0).getTitle());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(
            scripts = {
                    "/testData/clean.sql",
                    "/testData/insert_roles.sql",
                    "/testData/insert_users.sql",
                    "/testData/projects.sql",
                    "/testData/insert_task.sql",
                    "/testData/insert_labels.sql",
                    "/testData/insert_task_label.sql"
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get labels for a task successfully")
    void getTaskLabels_success() throws Exception {
        Long taskId = 1L;

        MvcResult mvcResult = mockMvc.perform(
                        get("/tasks/{id}/labels", taskId)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        LabelDto[] labels = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                LabelDto[].class
        );

        assertNotNull(labels);
        assertFalse(labels.length == 0, "Expected at least one label");
        assertNotNull(labels[0].getId());
        assertNotNull(labels[0].getName());
        assertNotNull(labels[0].getColor());
    }


    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T fromJson(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }
}
