package mate.academy.taskmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.taskmanagementapp.dto.comment.CommentDto;
import mate.academy.taskmanagementapp.dto.comment.CreateCommentRequestDto;
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
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql",
            "/testData/insert_task.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Create a new comment successfully")
    void addComment_successfully() throws Exception {
        Long taskId = 1L;

        CreateCommentRequestDto createCommentRequestDto = new CreateCommentRequestDto();
        createCommentRequestDto.setText("Test Comment");
        createCommentRequestDto.setTaskId(taskId);

        MvcResult mvcResult = mockMvc.perform(post("/api/comments")
                        .content(toJson(createCommentRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CommentDto commentDto = fromJson(mvcResult,  CommentDto.class);

        assertNotNull(commentDto);
        assertEquals(taskId, commentDto.getTaskId());
        assertEquals("Test Comment", commentDto.getText());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = {"USER"})
    @Sql(scripts = {
            "/testData/clean.sql",
            "/testData/insert_roles.sql",
            "/testData/insert_users.sql",
            "/testData/insert_task.sql",
            "/testData/insert_comment.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Get comments by taskId successfully")
    void getComment_successfully() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/comments")
                        .param("taskId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        CommentDto[] comments = fromJson(mvcResult, CommentDto[].class);

        assertNotNull(comments);
        assertTrue(comments.length > 0);

        assertEquals(1L, comments[0].getId());
        assertEquals(1L, comments[0].getTaskId());
        assertEquals("First comment", comments[0].getText());
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T fromJson(MvcResult result, Class<T> clazz) throws Exception {
        return objectMapper.readValue(result.getResponse().getContentAsString(), clazz);
    }
}